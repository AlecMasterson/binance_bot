import BaseApi from './BaseApi';
import DataUtil from '../util/DataUtil';
import TAMapper from '../types/TAMapper';

const INDICATOR_CONFIGS = 'http://localhost:8080/api/backtest/indicators';
const RULE_CONFIGS = 'http://localhost:8080/api/backtest/rules';
const CHART_DATA = 'http://localhost:8080/api/backtest/chartData';

export default class AppApi {
    static async loadConfigs() {
        const indicatorConfigs = await BaseApi.exchange(INDICATOR_CONFIGS);
        const ruleConfigs = await BaseApi.exchange(RULE_CONFIGS);

        return DataUtil.buildConfigMapper(indicatorConfigs, ruleConfigs);
    }

    static async getChartData(ticker: string, timePeriod: string, configs: TAMapper) {
        const request = {
            indicators: Object.values(configs),
            ticker,
            timePeriod
        };

        return await BaseApi.exchange(CHART_DATA, request);
    }
}