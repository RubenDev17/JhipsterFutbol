import { IEquipo } from 'app/shared/model/equipo.model';
import { IPartido } from 'app/shared/model/partido.model';

export interface IJugador {
  id?: number;
  nombre?: string;
  edad?: number;
  equipo?: IEquipo;
  partidos?: IPartido[];
}

export class Jugador implements IJugador {
  constructor(public id?: number, public nombre?: string, public edad?: number, public equipo?: IEquipo, public partidos?: IPartido[]) {}
}
