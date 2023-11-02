import { Operacion } from 'app/shared/model/enumerations/operacion.model';
import { ModoOrden } from 'app/shared/model/enumerations/modo-orden.model';
import { EstadoOrden } from 'app/shared/model/enumerations/estado-orden.model';

export interface IOrden {
  id?: number;
  numero?: string;
  cliente?: number;
  accionId?: number;
  accion?: string;
  operacion?: Operacion;
  precio?: number;
  cantidad?: number;
  fechaOperacion?: string;
  modo?: ModoOrden;
  estado?: EstadoOrden;
  descripcionEstado?: string | null;
}

export const defaultValue: Readonly<IOrden> = {};
