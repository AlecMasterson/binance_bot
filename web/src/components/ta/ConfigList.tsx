import React from 'react';
import TAMapper, {TAConfig} from '../../types/TAMapper';
import {Accordion, Card, CloseButton} from 'react-bootstrap';
import BootstrapTable from 'react-bootstrap-table-next';
import '../../styles/configList.css';

const FieldColumns = [
    {dataField: 'name', text: 'Field'},
    {dataField: 'value', text: 'Value'}
];

interface ConfigListProps {
    configs: TAMapper;
    deleteConfig: (configName: string) => void;
}

interface ConfigCardProps {
    config: TAConfig;
    deleteConfig: (configName: string) => void;
}

export function ConfigCard (props: ConfigCardProps) {
    const configuredFields = props.config.fields.filter(field => field.name !== 'Name');
    const fieldTable = (<BootstrapTable keyField='name' columns={FieldColumns} condensed={true} data={configuredFields} />);

    return (
        <Card key={props.config.name}>
            <Accordion.Toggle as={Card.Header} eventKey={props.config.name}>
                {props.config.name} - {props.config.type}
                <CloseButton onClick={() => props.deleteConfig(props.config.name)} />
            </Accordion.Toggle>

            <Accordion.Collapse eventKey={props.config.name}>
                <Card.Body className={configuredFields.length ? '' : 'empty-body'}>
                    {configuredFields.length ? fieldTable : 'No Fields Configured'}
                </Card.Body>
            </Accordion.Collapse>
        </Card>
    );
}

export default function (props: ConfigListProps) {
    const cards = Object.values(props.configs).map((config: TAConfig) => (
        <ConfigCard
            config={config}
            deleteConfig={props.deleteConfig} />));

    return (
        <div className='d-flex h-100 justify-content-center'>
            {!cards.length ?
                <h3 className='align-self-center'>No Configs Found</h3> :
                <Accordion className='w-100'>{cards}</Accordion>}
        </div>
    );
}