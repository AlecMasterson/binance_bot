import {ConfigType} from './ConfigType';

export default interface TAMapper {
    [name: string]: TAConfig
}

export interface TAConfig {
    baseType: ConfigType,
    description: string,
    fields: TAField[],
    name: string,
    type: string
}

export interface TAField {
    description: string,
    name: string,
    type: string,
    value: number | string
}