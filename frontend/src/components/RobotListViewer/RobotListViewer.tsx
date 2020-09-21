import React from 'react';
import './RobotListViewer.css';
import BambumRobot from "../../model/BambumRobot";
import App from "../../app/App";

interface RobotListViewerProps {
    app: App;
}


function RobotListViewer(props: RobotListViewerProps) {
    const app = props.app;

    return (
        <div className="RobotListViewer">
            <h2>My Robots</h2>
            <table className="RobotListViewer__table">
                {renderHeader()}
                <tbody>
                {app.robots.getAll().map(it => renderRobotListRobotEntry(it))}
                </tbody>
            </table>
            <div className="RobotListViewer__buttonList">
                <button className="RobotListViewer__button" onClick={e => app.startCreatingRobot()}>Create New</button>
                <button className="RobotListViewer__button" onClick={e => app.loadRobots()}>Reload</button>
            </div>
        </div>
    );


    /// RENDERING
    function renderHeader() {
        return (
            <thead>
            <tr className="RobotListViewer__header">
                <th className="RobotListViewer__headerCell">Name</th>
                <th className="RobotListViewer__headerCell">Lease Error</th>
                <th className="RobotListViewer__headerCell">Brain metadata</th>
                <th className="RobotListViewer__headerCell">Actions</th>
            </tr>
            </thead>
        );
    }

    function renderRobotListRobotEntry(robot:BambumRobot) {
        return (
            <tr
                key={robot.id}
                className="RobotListViewer__row"
                data-selected={robot.id == app.selectedId.get()}
                tabIndex={0}
                onClick={() => app.selectedId.set(getToggleSelection(app.selectedId.get(), robot.id))}>
                <td className="RobotListViewer__cell">
                    {robot.name}
                    <b className="RobotListViewer__robotId">{robot.id}</b>
                </td>
                <td className="RobotListViewer__cell">{robot.leastError}</td>
                {renderBrainMetaDaCell(robot)}
                <td className="RobotListViewer__cell">
                    <button className="RobotListViewer__cellBut" onClick={e => { e.stopPropagation(); deleteRobot(robot); }}>Delete</button>
                    {renderTrainingButton(robot)}
                    {robot.trainingData && "Training..."}
                </td>
            </tr>
        );
    }

    function renderBrainMetaDaCell(robot: BambumRobot) {
        if (!robot || !robot.brainMetadata) {
            return <td className="RobotListViewer__cell"/>;
        }

        const metadata = robot.brainMetadata;
        return <td className="RobotListViewer__cell">
            {metadata.layerCount} / {metadata.neuronCount} / {metadata.synapseCount}
        </td>
    }

    function renderTrainingButton(robot: BambumRobot) {
        return robot.trainingData == null?
            <button
                className="RobotListViewer__cellBut"
                onClick={e => { e.stopPropagation(); app.startTraining(robot.id); }}>
                Start Training
            </button> :
            <button
                className="RobotListViewer__cellBut"
                onClick={e => { e.stopPropagation(); app.stopTraining(robot.id); }}>
                Stop Training
            </button>;
    }


    /// UTILS
    function getToggleSelection(previousId:string, newId:string) : string {
        return previousId === newId? null : newId;
    }


    /// ACTIONS
    function deleteRobot(robot: BambumRobot) {
        const isSure = window.confirm(`Are you sure you want to delete robot ${robot.name}`);
        isSure && app.deleteRobot(robot.id);
    }
}

export default RobotListViewer;
