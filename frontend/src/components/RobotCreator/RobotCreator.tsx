import React, {useEffect, useRef} from 'react';
import './RobotCreator.css';
import App from "../../app/App";
import {useSubscriptions} from "../ComponentUtils";
import NumberInput from "../util/NumberInput";
import RobotCreationData from "../../app/RobotCreationData";

const DATA_PROVIDER_TYPES = [
    {label: "Addition", code:"ADD"},
    {label: "Subtraction", code:"SUB"}
];

const TRAINER_TYPES = [
    {label: "Batch trainer", code:"BATCH"},
];

interface RobotCreatorProps {
    app: App;
    data: RobotCreationData;
}

export default function RobotCreator(props: RobotCreatorProps) {
    useSubscriptions([
        props.data.name,
        props.data.dataProviderParams,
        props.data.trainerParams,
        props.data.dataProviderType,
        props.data.trainerType
    ]);

    const firstInputRef = useRef<HTMLInputElement>();
    useEffect(() => {
        firstInputRef.current && firstInputRef.current.focus();
    }, []);

    if (!props.data) return null;

    const state = props.data;
    const app = props.app;

    return <div className="RobotCreator">
        <div className="RobotCreator_window">
            {renderHeader()}
            {renderMain()}
            {renderFooter()}
        </div>
    </div>;


    /// RENDERING
    function renderHeader() {
        return <section className="RobotCreator_windowHeader">
            <h2>Create robot...</h2>
            <button className="RobotCreator_closeButton" onClick={e => app.cancelRobotCreation()}>X</button>
        </section>;
    }

    function renderMain() {
        return <section className="RobotCreator_main">
            <label className="App__label">
                <span>Name:</span>
                <input value={state.name.get() || ""} onChange={e => state.name.set(e.target.value)} ref={firstInputRef}/>
            </label>

            <label className="App__label">
                <span>Data Provider:</span>
                <select value={state.dataProviderType.get()} onChange={e => state.dataProviderType.set(e.target.value)}>
                    <option value="" disabled>Please select type</option>
                    {DATA_PROVIDER_TYPES.map(dpt => <option key={dpt.code} value={dpt.code}>{dpt.label}</option>)}
                </select>
            </label>

            {renderDataProviderParameters(state.dataProviderType.get())}

            <label className="App__label">
                <span>Trainer:</span>
                <select value={state.trainerType.get()} onChange={e => state.trainerType.set(e.target.value)}>
                    <option value="" disabled>Please select type</option>
                    {TRAINER_TYPES.map(dpt => <option key={dpt.code} value={dpt.code}>{dpt.label}</option>)}
                </select>
            </label>

            {renderTrainerParameters(state.trainerType.get())}
        </section>;
    }

    function renderFooter() {
        return <section className="RobotCreator_footer">
            <button onClick={() => app.submitCreationData()} disabled={!state.canSubmit()}>Save</button>
        </section>;
    }

    function renderDataProviderParameters(type: string) {
        if (!type) return null;

        switch (type) {
            case "ADD":
            case "SUB":
                return <section className="RobotCreator_paramsSection">
                    <NumberInput
                        label="Range"
                        value={state.dataProviderParams.get("range")}
                        onChange={e => state.dataProviderParams.set("range", e)} />
                </section>;

            default:
                return <div>Unknown params TODO</div>;
        }
    }

    function renderTrainerParameters(type: string) {
        if (!type) return null;

        switch (type) {
            case "BATCH":
                return <section className="RobotCreator_paramsSection">
                    <NumberInput
                        label="Batch size:"
                        value={state.trainerParams.get("batchSize")}
                        onChange={v => state.trainerParams.set("batchSize", v)}/>
                    <NumberInput
                        label="Learning rate:"
                        value={state.trainerParams.get("learningRate")}
                        onChange={v => state.trainerParams.set("learningRate", v)} step={0.001}/>
                    <NumberInput
                        label="Momentum:"
                        value={state.trainerParams.get("momentum")}
                        onChange={v => state.trainerParams.set("momentum", v)} step={0.0001}/>
                </section>;

            default:
                return <div>Unknown params TODO</div>;
        }
    }
}
