import BambumRobot from "./BambumRobot";
import TrainingData from "./TrainingData";

export default class Mutation {
    createRobot: BambumRobot;
    deleteRobot: boolean;

    loadBestState: boolean;
    loadState: boolean;

    startTraining : TrainingData;
    stopTraining: TrainingData;
}
