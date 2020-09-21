import DataProviderConfig from "../model/DataProviderConfig";
import TrainerConfig from "../model/TrainerConfig";
import RwLeaf from "./state/leaf/RwLeaf";
import RwMap from "./state/maps/RwMap";

export default class RobotCreationData {
    /// STATE
    readonly name = new RwLeaf("");
    readonly dataProviderParams = new RwMap<string>({});
    readonly trainerParams = new RwMap<string>({});

    readonly dataProviderType = new RwLeaf("", (v) => this.setDataProviderType(v));
    readonly trainerType = new RwLeaf("", (v) => this.setTrainerType(v));

    networkConfig: any = {
        "layers":[
            {"type":"FULLY_CONNECTED","neuronCount":784},
            {"type":"FULLY_CONNECTED","neuronCount":15,"times":2},
            {"type":"OUT","neuronCount":10,"activation":"LINEAR"}
        ],
        "activation":"LEAKY_RELU"
    };

    /// INFOS
    canSubmit() {
        if (!this.name.get() || !this.dataProviderType.get()) return false;

        switch (this.dataProviderType.get()) {
            case "ADD":
            case "SUB": {
                const stringValue = this.dataProviderParams.get("range");
                const value = parseInt(stringValue);
                if (!stringValue || isNaN(value) || value === 0) {
                    return false;
                }
                break;
            }
        }

        return true;
    }

    createDataProviderConfig() : DataProviderConfig {
        return {
            type: this.dataProviderType.get(),
            data: this.dataProviderParams.getAll()
        };
    }

    createTrainerConfig() : TrainerConfig {
        return {type: this.trainerType.get(), data: this.trainerParams.getAll()};
    }

    /// ACTIONS
    private setDataProviderType(value: string) {
        if (this.dataProviderType.get() === value) return;
        this.dataProviderType.next(value);
        this.dataProviderParams.setAll({});

        return true;
    }

    private setTrainerType(value: string) {
        if (this.trainerType.get() === value) return false;
        this.trainerType.next(value);
        this.trainerParams.setAll({});

        return true;
    }
}
