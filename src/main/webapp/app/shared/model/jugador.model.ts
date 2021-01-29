import { IEquipo } from 'app/shared/model/equipo.model';

export interface IJugador {
  id?: number;
  nombre?: string;
  edad?: number;
  equipo?: IEquipo;
}

export class Jugador implements IJugador {
  constructor(public id?: number, public nombre?: string, public edad?: number, public equipo?: IEquipo) {}
}
