import React from 'react';
import './RobotViewer.css';
import BambumRobot from "../../model/BambumRobot";
import TrainingData from "../../model/TrainingData";

interface RobotViewerProps {
    robot: BambumRobot;
}

function RobotViewer(props: RobotViewerProps) {
    if (props.robot == null) return null;

    return (
        <div className="RobotViewer">
            {renderMainData(props.robot)}
        </div>
    );
}


function renderMainData(robot: BambumRobot) {
    const brainMeta = robot.brainMetadata;

    return(
        <div className="RobotViewer__mainData">
            <b>Name:</b>
            <span>{robot.name}</span>

            <b>Id:</b>
            <span>{robot.id}</span>

            <b>Least error:</b>
            <span>{robot.leastError}</span>

            <b>Brain layers:</b>
            <span>{brainMeta == null? "?" : brainMeta.layerCount}</span>

            <b>Neurons:</b>
            <span>{brainMeta == null? "?" : brainMeta.neuronCount}</span>

            <b>Synapses:</b>
            <span>{brainMeta == null? "?" : brainMeta.synapseCount}</span>

            <b>Data Provider:</b>
            <span>{robot.dataProviderConfig == null? "?" : robot.dataProviderConfig.type}</span>

            <b>Trainer:</b>
            <span>{robot.trainerConfig == null? "?" : robot.trainerConfig.type}</span>

            <b>Is Training:</b>
            <span>{robot.trainingData == null? "No" : "Yes"}</span>

            {renderTrainingData(robot.trainingData)}
        </div>
    );
}

function renderTrainingData(trainingData: TrainingData) {
    if (!trainingData) return null;

    return <>
        <b>Error Before training:</b>
        <span>{trainingData.errorBeforeTraining}</span>

        <b>Minimum error:</b>
        <span>{trainingData.minimumError}</span>
    </>;
}

export default RobotViewer;
