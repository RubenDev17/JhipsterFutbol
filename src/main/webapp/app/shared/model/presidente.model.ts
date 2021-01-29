import { IEquipo } from 'app/shared/model/equipo.model';

export interface IPresidente {
  id?: number;
  nombre?: string;
  aniosEnPresidencia?: number;
  equipo?: IEquipo;
}

export class Presidente implements IPresidente {
  constructor(public id?: number, public nombre?: string, public aniosEnPresidencia?: number, public equipo?: IEquipo) {}
}
