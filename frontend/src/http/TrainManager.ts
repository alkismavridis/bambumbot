import Graphql from "./Graphql";
import TrainingData from "../model/TrainingData";

export default class TrainManager {
    startTraining(id: string) : Promise<TrainingData> {
        return Graphql
            .mutation(`mutation($id: ID!) { startTraining(robotId: $id) { errorBeforeTraining, minimumError} }`, {id: id})
            .then(m => m.startTraining);
    }

    stopTraining(id: string) : Promise<TrainingData> {
        return Graphql
            .mutation(`mutation($id: ID!) { stopTraining(robotId: $id) { errorBeforeTraining, minimumError} }`, {id: id})
            .then(m => m.stopTraining);

    }
}
