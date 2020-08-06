import React from 'react';
import AppApi from '../api/AppApi';
import TAMapper from '../types/TAMapper';
import ConfigForm from './ta/ConfigForm';
import {ConfigList} from './ta/ConfigList';
import AnalChart from './AnalChart';
import '../styles/backtest.css';

interface AnalState {
    chartData: object;
    configs: TAMapper;
    interval: string;
    ticker: string;
}

export default class Anal extends React.Component<any, AnalState> {

    constructor(props: any) {
        super(props);

        this.state = {
            chartData: {},
            configs: {},
            interval: '',
            ticker: ''
        };

        this.updateData = this.updateData.bind(this);
        this.deleteConfig = this.deleteConfig.bind(this);
    }

    updateData(configs: TAMapper, interval: string, ticker: string): void {
        this.setState({configs, interval, ticker}, async () => {
            const chartData = await AppApi.getChartData(this.state.ticker, this.state.interval, this.state.configs);
            this.setState({chartData});
        });
    }

    deleteConfig(configName: string): void {
        delete this.state.configs[configName];
        this.setState({configs: this.state.configs});
    }

    render() {
        return (
            <>
                <div className='row' style={{marginTop: '1rem'}}>
                    <div className='col'>
                        <ConfigForm updateAnalData={this.updateData} />
                    </div>

                    <div className='col'>
                        <ConfigList configs={this.state.configs} deleteConfig={this.deleteConfig} />
                    </div>
                </div>

                <div className='row'>
                    <AnalChart chartData={this.state.chartData} />
                </div>
            </>
        );
    }
}