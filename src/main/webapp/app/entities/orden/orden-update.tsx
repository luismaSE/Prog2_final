import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IOrden } from 'app/shared/model/orden.model';
import { Operacion } from 'app/shared/model/enumerations/operacion.model';
import { Modo } from 'app/shared/model/enumerations/modo.model';
import { Estado } from 'app/shared/model/enumerations/estado.model';
import { getEntity, updateEntity, createEntity, reset } from './orden.reducer';

export const OrdenUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const ordenEntity = useAppSelector(state => state.orden.entity);
  const loading = useAppSelector(state => state.orden.loading);
  const updating = useAppSelector(state => state.orden.updating);
  const updateSuccess = useAppSelector(state => state.orden.updateSuccess);
  const operacionValues = Object.keys(Operacion);
  const modoValues = Object.keys(Modo);
  const estadoValues = Object.keys(Estado);

  const handleClose = () => {
    navigate('/orden');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...ordenEntity,
      ...values,
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          operacion: 'COMPRA',
          modo: 'AHORA',
          estado: 'OK',
          ...ordenEntity,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="prog2FinalApp.orden.home.createOrEditLabel" data-cy="OrdenCreateUpdateHeading">
            <Translate contentKey="prog2FinalApp.orden.home.createOrEditLabel">Create or edit a Orden</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="orden-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('prog2FinalApp.orden.cliente')}
                id="orden-cliente"
                name="cliente"
                data-cy="cliente"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('prog2FinalApp.orden.accionId')}
                id="orden-accionId"
                name="accionId"
                data-cy="accionId"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('prog2FinalApp.orden.accion')}
                id="orden-accion"
                name="accion"
                data-cy="accion"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('prog2FinalApp.orden.operacion')}
                id="orden-operacion"
                name="operacion"
                data-cy="operacion"
                type="select"
              >
                {operacionValues.map(operacion => (
                  <option value={operacion} key={operacion}>
                    {translate('prog2FinalApp.Operacion.' + operacion)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('prog2FinalApp.orden.precio')}
                id="orden-precio"
                name="precio"
                data-cy="precio"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('prog2FinalApp.orden.cantidad')}
                id="orden-cantidad"
                name="cantidad"
                data-cy="cantidad"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('prog2FinalApp.orden.fechaOperacion')}
                id="orden-fechaOperacion"
                name="fechaOperacion"
                data-cy="fechaOperacion"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField label={translate('prog2FinalApp.orden.modo')} id="orden-modo" name="modo" data-cy="modo" type="select">
                {modoValues.map(modo => (
                  <option value={modo} key={modo}>
                    {translate('prog2FinalApp.Modo.' + modo)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('prog2FinalApp.orden.estado')}
                id="orden-estado"
                name="estado"
                data-cy="estado"
                type="select"
              >
                {estadoValues.map(estado => (
                  <option value={estado} key={estado}>
                    {translate('prog2FinalApp.Estado.' + estado)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('prog2FinalApp.orden.descripcionEstado')}
                id="orden-descripcionEstado"
                name="descripcionEstado"
                data-cy="descripcionEstado"
                type="text"
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/orden" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default OrdenUpdate;
