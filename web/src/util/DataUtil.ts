import TAMapper, {TAConfig} from '../types/TAMapper';
import {ConfigType} from '../types/ConfigType';

export default class DataUtil {
    static buildConfigMapper(indicatorConfigs: TAConfig[], ruleConfigs: TAConfig[]): TAMapper {
        return indicatorConfigs.concat(ruleConfigs).reduce((obj: TAMapper, config: TAConfig) => Object.assign(obj, {[config.type]: config}), {});
    }

    static filterTAMapper(configs: TAMapper, configType: ConfigType): TAConfig[] {
        return Object.values(configs).filter(config => config.baseType === configType);
    }

    static getIndicatorConfigsByName(configs: TAMapper): TAMapper {
        return this.filterTAMapper(configs, ConfigType.INDICATOR)
            .reduce((obj: TAMapper, config: TAConfig) => Object.assign(obj, {[config.name]: config}), {});
    }

    static getIndicatorConfigsByType(configs: TAMapper): TAMapper {
        return this.filterTAMapper(configs, ConfigType.INDICATOR)
            .reduce((obj: TAMapper, config: TAConfig) => Object.assign(obj, {[config.type]: config}), {});
    }

    static getRuleConfigsByName(configs: TAMapper): TAMapper {
        return this.filterTAMapper(configs, ConfigType.RULE)
            .reduce((obj: TAMapper, config: TAConfig) => Object.assign(obj, {[config.name]: config}), {});
    }

    static getRuleConfigsByType(configs: TAMapper): TAMapper {
        return this.filterTAMapper(configs, ConfigType.RULE)
            .reduce((obj: TAMapper, config: TAConfig) => Object.assign(obj, {[config.type]: config}), {});
    }
}