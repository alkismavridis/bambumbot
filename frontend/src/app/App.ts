import BambumRobot from "../model/BambumRobot";
import RobotCreationData from "./RobotCreationData";
import RobotRepository from "../http/RobotRepository";
import TrainManager from "../http/TrainManager";
import RwLeaf from "./state/leaf/RwLeaf";
import RwArray from "./state/arrays/RwArray";

export default class App {
    /// STATE
    readonly robots = new RwArray<BambumRobot>([]);
    readonly selectedId = new RwLeaf("");
    readonly robotCreationData = new RwLeaf<RobotCreationData>(null);


    /// SETUP
    constructor() {
        this.loadRobots();
    }


    /// INFOS
    getSelectedRobot() : BambumRobot {
        return this.getRobot(this.selectedId.get());
    }

    getRobot(id: string) {
        if (id == null) return null;
        return this.robots.getAll().find(r => r.id === id);
    }


    /// ACTIONS
    deleteRobot(id: string) {
        new RobotRepository().delete(id)
            .then(() => {
                const newRobots = this.robots.getAll().filter(r => r.id != id);
                this.robots.setAll(newRobots);

                if (id === this.selectedId.get()) {
                    this.selectedId.set(null);
                }
            });
    }


    // Robot Creation related
    startCreatingRobot() {
        this.robotCreationData.set(new RobotCreationData());
    }

    submitCreationData() {
        const value = this.robotCreationData.get();
        if (value == null || !value.canSubmit()) return;

        new RobotRepository().save(
            value.name.get(),
            value.networkConfig,
            value.createDataProviderConfig(),
            value.createTrainerConfig(),
        ).then(newRobot => {
            this.robots.push(newRobot);
            this.robotCreationData.set(null);
        }).catch(() => window.alert("Could not save robot."));
    }

    cancelRobotCreation() {
        this.robotCreationData.set(null);
    }

    loadRobots() {
        new RobotRepository().getAll()
            .then(robots => {
                this.robots.setAll(robots);
            })
            .catch(e => console.error("Could not fetch robot list."))
    }

    startTraining(id: string) {
        const robot = this.getRobot(id);
        if (robot == null) return;

        new TrainManager()
            .startTraining(robot.id)
            .then(trainData => {
                robot.trainingData = trainData;
                this.robots.fire();
            })
            .catch(() => window.alert("Could not start training."));
    }

    stopTraining(id: string) {
        const robot = this.getRobot(id);
        if (robot == null) return;

        new TrainManager()
            .stopTraining(robot.id)
            .then(trainData => {
                robot.trainingData = null;
                robot.leastError = trainData.minimumError;
                this.robots.fire();
            })
            .catch(() => window.alert("Could not stop training."));
    }
}
