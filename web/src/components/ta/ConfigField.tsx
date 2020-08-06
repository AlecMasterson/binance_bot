import React, {ChangeEvent} from 'react';
import {Form, FormControl} from 'react-bootstrap';
import TAMapper, {TAConfig, TAField} from '../../types/TAMapper';
import ItemSelector from '../common/ItemSelector';

interface ConfigFieldProps {
    config: TAConfig,
    currentConfigs: TAMapper,
    field: TAField,
    updateConfig: (config: TAConfig) => void
}

interface ConfigFieldState {
    selectedConfig: string
}

export default class ConfigField extends React.Component<ConfigFieldProps, ConfigFieldState> {

    constructor(props: ConfigFieldProps) {
        super(props);

        this.state = {
            selectedConfig: ''
        }

        this.onChange = this.onChange.bind(this);
    }

    onChange(event: ChangeEvent<HTMLInputElement> | ChangeEvent<HTMLSelectElement>): void {
        this.setState({selectedConfig: event.target.value}, () => {
            const fieldIndex = this.props.config.fields.map(field => field.name).indexOf(this.props.field.name);

            const newFields = [
                ...this.props.config.fields.slice(0, fieldIndex),
                {...this.props.config.fields[fieldIndex], value: this.state.selectedConfig},
                ...this.props.config.fields.slice(fieldIndex + 1)
            ];

            this.props.updateConfig({
                ...this.props.config,
                name: this.props.field.name === 'Name' ? this.state.selectedConfig : this.props.config.name,
                fields: newFields
            });
        });
    }

    render() {
        let input;
        switch (this.props.field.type) {
            case 'INDICATOR_NUM':
                input = (<ItemSelector items={Object.keys(this.props.currentConfigs)} onChange={this.onChange} value={this.state.selectedConfig}/>);
                break;
            case 'RULE':
                input = (<ItemSelector items={Object.keys(this.props.currentConfigs)} onChange={this.onChange} value={this.state.selectedConfig}/>);
                break;
            case 'INT':
            case 'DOUBLE':
                input = (
                    <FormControl type='number' onChange={this.onChange} placeholder={this.props.field.name} required={true}/>);
                break;
            case 'STRING':
                input = (<FormControl type='text' onChange={this.onChange} placeholder={this.props.field.name} required={true}/>);
                break;
            default:
                throw Error('Unknown Field Type Found');
        }

        return (
            <Form.Group key={this.props.field.name}>
                <Form.Label>
                    {this.props.field.name}
                </Form.Label>

                {input}
                <small className='label-subtext'>{this.props.field.description}</small>
            </Form.Group>
        );
    }
}