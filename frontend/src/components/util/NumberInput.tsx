import React from 'react';

interface Props {
    key?: any;
    label: string;
    value: string;
    step?: number;
    onChange: (str:string) => void;
}

export default function NumberInput(props: Props) {
    return <label className="App__label">
        <span>{props.label}</span>
        <input
            type="number"
            value={props.value || ""}
            step={props.step}
            onChange={props.onChange && (e => props.onChange(e.target.value))} />
    </label>;
}
