import React, {FormEvent} from 'react';
import {Button, Form, Modal} from 'react-bootstrap';
import DataUtil from '../../util/DataUtil';
import TAMapper, {TAConfig} from '../../types/TAMapper';
import {ConfigType} from '../../types/ConfigType';
import ConfigField from './ConfigField';

interface ConfigBuilderProps {
    config: TAConfig,
    currentConfigs: TAMapper,
    onHide: () => void,
    saveConfig: (config: TAConfig) => void,
    show: boolean
}

interface ConfigBuilderState {
    config: TAConfig
}

export default class ConfigBuilder extends React.Component<ConfigBuilderProps, ConfigBuilderState> {

    constructor(props: ConfigBuilderProps) {
        super(props);

        this.state = {
            config: props.config
        };

        this.onSubmit = this.onSubmit.bind(this);
    }

    componentDidUpdate(prevProps: ConfigBuilderProps) {
        if (prevProps !== this.props) {
            this.setState({config: this.props.config});
        }
    }

    onSubmit(event: FormEvent<HTMLFormElement>): void {
        event.preventDefault();
        this.props.saveConfig(this.state.config);
    }

    render() {
        if (!this.state.config) {
            return null;
        }
        const configFields = this.state.config.fields.map(field => {
            const configs = this.state.config.baseType === ConfigType.INDICATOR ?
                DataUtil.getIndicatorConfigsByName(this.props.currentConfigs) :
                DataUtil.getRuleConfigsByName(this.props.currentConfigs);

            return (
                <ConfigField
                    key={field.name}
                    config={this.state.config}
                    currentConfigs={configs}
                    field={field}
                    updateConfig={(config: TAConfig) => this.setState({config})} />
             );
        });

        return (
            <Modal show={this.props.show} onHide={this.props.onHide} centered>
                <Modal.Header closeButton>
                    <Modal.Title>
                        {this.state.config.type}
                        <br/>
                        <small className='label-subtext'>{this.state.config.description}</small>
                    </Modal.Title>
                </Modal.Header>

                <Modal.Body>
                    <Form>
                    </Form>
                    <form id='config-form' onSubmit={this.onSubmit}>
                        {configFields}
                    </form>
                </Modal.Body>

                <Modal.Footer>
                    <Button variant='danger' onClick={this.props.onHide}>Cancel</Button>
                    <Button type='submit' form='config-form'>Add</Button>
                </Modal.Footer>
            </Modal>
        );
    }
}