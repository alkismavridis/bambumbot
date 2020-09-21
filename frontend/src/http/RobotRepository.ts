import BambumRobot from "../model/BambumRobot";
import Graphql from "./Graphql";
import DataProviderConfig from "../model/DataProviderConfig";
import TrainerConfig from "../model/TrainerConfig";

export default class RobotRepository {
    getAll() : Promise<BambumRobot[]> {
        return Graphql
            .run(`{ robots {
                    id, name, leastError
                    brainMetadata { layerCount, neuronCount, synapseCount, inputSize, outputSize}
                    trainerConfig { type, data }
                    dataProviderConfig { type, data }
                    trainingData { errorBeforeTraining, minimumError }
                }}`)
            .then((data:any) => data.robots);
    }

    save(name: string, networkConfig: any, dataProviderConfig: DataProviderConfig, trainerConfig: TrainerConfig) : Promise<BambumRobot> {
        const variables = {name: name, networkConfig: networkConfig, dataProviderConfig: dataProviderConfig, trainerConfig: trainerConfig}
        const mutation = `mutation($name: String!, $networkConfig: NetworkConfigInput!, $dataProviderConfig: DataProviderConfigInput!, $trainerConfig: TrainerConfigInput!) {
            createRobot(name: $name, networkConfig: $networkConfig, dataProviderConfig: $dataProviderConfig, trainerConfig: $trainerConfig) {
                id, name, leastError
                brainMetadata { layerCount, neuronCount, synapseCount, inputSize, outputSize}
                trainerConfig { type, data }
                dataProviderConfig { type, data }
            }
        }`;

        return Graphql.mutation(mutation, variables).then(m => m.createRobot);
    }

    delete(id: string) : Promise<void> {
        return Graphql
            .mutation(`mutation($id: ID!) { deleteRobot(robotId: $id)}`, {id: id})
            .then(m => null);
    }


    /// UTILS
    // private makeRobot(id: string, name: string): BambumRobot {
    //     return {
    //         id: id,
    //         name: name,
    //         leastError: 123,
    //         brainMetadata: null,
    //         trainingData: null
    //     };
    // }
    //
    // private getAllDummy() : Promise<BambumRobot[]> {
    //     return new Promise<BambumRobot[]>((resolve, reject) => {
    //         resolve([
    //             this.makeRobot("05de40cb-728f-4b17-a9c9-961d32592f8b", "Addy-50"),
    //             this.makeRobot("24b7e028-f882-48f5-bf71-7d737819e4a1", "Addy-100"),
    //             this.makeRobot("24b7e028-f882-48f5-bf71-7d737819e4a2", "Addy-200"),
    //             this.makeRobot("24b7e028-f882-48f5-bf71-7d737819e4a3", "Addy-300"),
    //             this.makeRobot("24b7e028-f882-48f5-bf71-7d737819e4a4", "Addy-400"),
    //             this.makeRobot("24b7e028-f882-48f5-bf71-7d737819e4a5", "Addy-500"),
    //             this.makeRobot("24b7e028-f882-48f5-bf71-7d737819e4a6", "Addy-600"),
    //         ]);
    //     });
    // }
}
