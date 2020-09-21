import BrainMetadata from "./BrainMetadata";
import TrainingData from "./TrainingData";
import TrainerConfig from "./TrainerConfig";
import DataProviderConfig from "./DataProviderConfig";

export default class BambumRobot {
    id: string = "";
    name: string = "";
    brainMetadata: BrainMetadata;

    dataProviderConfig: DataProviderConfig;
    trainerConfig: TrainerConfig;

    bestState: number[];
    leastError: number = 9999999999;
    currentState: number[];

    trainingData: TrainingData;
}
