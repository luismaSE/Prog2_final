import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './orden.reducer';

export const OrdenDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const ordenEntity = useAppSelector(state => state.orden.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="ordenDetailsHeading">
          <Translate contentKey="prog2FinalApp.orden.detail.title">Orden</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{ordenEntity.id}</dd>
          <dt>
            <span id="cliente">
              <Translate contentKey="prog2FinalApp.orden.cliente">Cliente</Translate>
            </span>
          </dt>
          <dd>{ordenEntity.cliente}</dd>
          <dt>
            <span id="accionId">
              <Translate contentKey="prog2FinalApp.orden.accionId">Accion Id</Translate>
            </span>
          </dt>
          <dd>{ordenEntity.accionId}</dd>
          <dt>
            <span id="accion">
              <Translate contentKey="prog2FinalApp.orden.accion">Accion</Translate>
            </span>
          </dt>
          <dd>{ordenEntity.accion}</dd>
          <dt>
            <span id="operacion">
              <Translate contentKey="prog2FinalApp.orden.operacion">Operacion</Translate>
            </span>
          </dt>
          <dd>{ordenEntity.operacion}</dd>
          <dt>
            <span id="precio">
              <Translate contentKey="prog2FinalApp.orden.precio">Precio</Translate>
            </span>
          </dt>
          <dd>{ordenEntity.precio}</dd>
          <dt>
            <span id="cantidad">
              <Translate contentKey="prog2FinalApp.orden.cantidad">Cantidad</Translate>
            </span>
          </dt>
          <dd>{ordenEntity.cantidad}</dd>
          <dt>
            <span id="fechaOperacion">
              <Translate contentKey="prog2FinalApp.orden.fechaOperacion">Fecha Operacion</Translate>
            </span>
          </dt>
          <dd>{ordenEntity.fechaOperacion}</dd>
          <dt>
            <span id="modo">
              <Translate contentKey="prog2FinalApp.orden.modo">Modo</Translate>
            </span>
          </dt>
          <dd>{ordenEntity.modo}</dd>
          <dt>
            <span id="estado">
              <Translate contentKey="prog2FinalApp.orden.estado">Estado</Translate>
            </span>
          </dt>
          <dd>{ordenEntity.estado}</dd>
          <dt>
            <span id="descripcionEstado">
              <Translate contentKey="prog2FinalApp.orden.descripcionEstado">Descripcion Estado</Translate>
            </span>
          </dt>
          <dd>{ordenEntity.descripcionEstado}</dd>
        </dl>
        <Button tag={Link} to="/orden" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/orden/${ordenEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default OrdenDetail;
