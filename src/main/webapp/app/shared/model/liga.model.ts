import { ITemporada } from 'app/shared/model/temporada.model';

export interface ILiga {
  id?: number;
  pais?: string;
  nombre?: string;
  temporada?: ITemporada;
}

export class Liga implements ILiga {
  constructor(public id?: number, public pais?: string, public nombre?: string, public temporada?: ITemporada) {}
}
