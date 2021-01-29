import { Moment } from 'moment';

export interface IEquipo {
  id?: number;
  nombre?: string;
  titulos?: number;
  fechaDeFundacion?: Moment;
}

export class Equipo implements IEquipo {
  constructor(public id?: number, public nombre?: string, public titulos?: number, public fechaDeFundacion?: Moment) {}
}
