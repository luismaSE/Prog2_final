import dayjs from 'dayjs';
import { Operacion } from 'app/shared/model/enumerations/operacion.model';
import { Modo } from 'app/shared/model/enumerations/modo.model';
import { Estado } from 'app/shared/model/enumerations/estado.model';

export interface IOrden {
  id?: number;
  cliente?: number;
  accionId?: number;
  accion?: string;
  operacion?: Operacion;
  precio?: number;
  cantidad?: number;
  modo?: Modo;
  estado?: Estado | null;
  descripcionEstado?: string | null;
  fechaOperacion?: string;
}

export const defaultValue: Readonly<IOrden> = {};
