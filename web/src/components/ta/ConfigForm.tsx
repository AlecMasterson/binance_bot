import React, {ChangeEvent} from 'react';
import {Button, Form} from 'react-bootstrap';
import AppApi from '../../api/AppApi';
import DataUtil from '../../util/DataUtil';
import TAMapper, {TAConfig} from '../../types/TAMapper';
import ItemSelector from '../common/ItemSelector';
import ConfigBuilder from './ConfigBuilder';

interface ConfigFormProps {
    updateAnalData: (configs: TAMapper, interval: string, ticker: string) => void;
}

interface ConfigFormState {
    currentConfigs: TAMapper;
    defaultConfigs: TAMapper;
    defaultIntervals: string[];
    defaultTickers: string[];
    selectedConfig: string;
    selectedInterval: string;
    selectedTicker: string;
    showBuilder: boolean;
}

export default class ConfigForm extends React.Component<ConfigFormProps, ConfigFormState> {

    constructor(props: ConfigFormProps) {
        super(props);

        this.state = {
            currentConfigs: {},
            defaultConfigs: {},
            defaultIntervals: [],
            defaultTickers: [],
            selectedConfig: '',
            selectedInterval: '',
            selectedTicker: '',
            showBuilder: false
        };

        this.saveConfig = this.saveConfig.bind(this);

        this.hideBuilder = this.hideBuilder.bind(this);
        this.showBuilder = this.showBuilder.bind(this);

        this.onChangeConfig = this.onChangeConfig.bind(this);
        this.onChangeInterval = this.onChangeInterval.bind(this);
        this.onChangeTicker = this.onChangeTicker.bind(this);

        this.updateAnalData = this.updateAnalData.bind(this);
    }

    async componentDidMount() {
        const defaultConfigs = await AppApi.getDefaultConfigs();
        const defaultIntervals = await AppApi.getIntervals();
        const defaultTickers = await AppApi.getTickers();

        this.setState({defaultConfigs, defaultIntervals, defaultTickers});
    }

    saveConfig(config: TAConfig): void {
        const newConfigs = {...this.state.currentConfigs, [config.name]: config};
        this.setState({currentConfigs: newConfigs, showBuilder: false}, () => this.updateAnalData());
    }

    hideBuilder(): void {
        this.setState({showBuilder: false});
    }

    showBuilder(): void {
        this.setState({showBuilder: true});
    }

    onChangeConfig(event: ChangeEvent<HTMLSelectElement>): void {
        this.setState({selectedConfig: event.target.value});
    }

    onChangeInterval(event: ChangeEvent<HTMLSelectElement>): void {
        this.setState({selectedInterval: event.target.value}, () => this.updateAnalData());
    }

    onChangeTicker(event: ChangeEvent<HTMLSelectElement>): void {
        this.setState({selectedTicker: event.target.value}, () => this.updateAnalData());
    }

    updateAnalData(): void {
        const {currentConfigs, selectedInterval, selectedTicker} = this.state;
        this.props.updateAnalData(currentConfigs, selectedInterval, selectedTicker);
    }

    render() {
        const {currentConfigs, defaultConfigs, defaultIntervals, defaultTickers, selectedConfig, selectedInterval, selectedTicker, showBuilder} = this.state;

        const indicatorConfigs = DataUtil.getIndicatorConfigsByType(defaultConfigs);
        const ruleConfigs = DataUtil.getRuleConfigsByType(defaultConfigs);

        const configGroups = [
            {items: Object.keys(indicatorConfigs), label: 'Data Indicators'},
            {items: Object.keys(ruleConfigs), label: 'Strategy Rules'}
        ];

        return (
            <Form>
                <Form.Group>
                    <Form.Label>Time Interval</Form.Label>
                    <ItemSelector items={[{items: defaultIntervals, label: 'Intervals'}]} onChange={this.onChangeInterval} value={selectedInterval} />
                </Form.Group>

                <Form.Group>
                    <Form.Label>Ticker</Form.Label>
                    <ItemSelector items={[{items: defaultTickers, label: 'Tickers'}]} onChange={this.onChangeTicker} value={selectedTicker} />
                </Form.Group>

                <Form.Group>
                    <Form.Label>Create Strategy Config</Form.Label>
                    <ItemSelector items={configGroups} onChange={this.onChangeConfig} value={selectedConfig} />

                    <Button className='add-config-button' onClick={this.showBuilder} disabled={selectedConfig === ''}>
                        Create
                    </Button>

                    <ConfigBuilder
                        config={defaultConfigs[selectedConfig]}
                        currentConfigs={currentConfigs}
                        onHide={this.hideBuilder}
                        saveConfig={this.saveConfig}
                        show={showBuilder} />
                </Form.Group>
            </Form>
        );
    }
}