import React, {ChangeEvent} from 'react';

interface ItemsSelectorProps {
    items: string[],
    onChange: (event: ChangeEvent<HTMLSelectElement>) => void,
    value: string
}

export default function (props: ItemsSelectorProps) {
    const options = props.items.map(item => (<option key={item}>{item}</option>));

    return (
        <select className='form-control' onChange={props.onChange} value={props.value} required={true}>
            <option/>
            {options}
        </select>
    );
}