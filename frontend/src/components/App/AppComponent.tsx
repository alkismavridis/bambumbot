import React, {useState} from 'react';
import './AppComponent.css';
import RobotListViewer from "../RobotListViewer/RobotListViewer";
import RobotViewer from "../RobotViewer/RobotViewer";
import RobotCreator from "../RobotCreator/RobotCreator";
import App from "../../app/App";
import {useSubscriptions} from "../ComponentUtils";
import ImageMatrixViewer from "../ImageMatrixViewer/ImageMatrixViewer";

function AppComponent() {
    const [app] = useState(() => new App());
    useSubscriptions([
        app.selectedId,
        app.robotCreationData,
        app.robots,
    ])

    if (!app) return null;

    if (window.location.pathname === "/images") {
        return <div className="App">
            <ImageMatrixViewer />
        </div>;
    }

    const selectedRobot = app.getSelectedRobot();
    return (
        <div className="App">
            <RobotListViewer app={app}/>
            <RobotViewer robot={selectedRobot}/>
            {app.robotCreationData.get() && <RobotCreator app={app} data={app.robotCreationData.get()}/>}
        </div>
    );
}

export default AppComponent;
