import { Moment } from 'moment';
import { ILiga } from 'app/shared/model/liga.model';

export interface IEquipo {
  id?: number;
  nombre?: string;
  titulos?: number;
  fechaDeFundacion?: Moment;
  liga?: ILiga;
}

export class Equipo implements IEquipo {
  constructor(public id?: number, public nombre?: string, public titulos?: number, public fechaDeFundacion?: Moment, public liga?: ILiga) {}
}
