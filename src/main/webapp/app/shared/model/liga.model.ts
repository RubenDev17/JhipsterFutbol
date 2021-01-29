import { ITemporada } from 'app/shared/model/temporada.model';
import { IEquipo } from 'app/shared/model/equipo.model';

export interface ILiga {
  id?: number;
  pais?: string;
  nombre?: string;
  temporada?: ITemporada;
  equipos?: IEquipo[];
}

export class Liga implements ILiga {
  constructor(
    public id?: number,
    public pais?: string,
    public nombre?: string,
    public temporada?: ITemporada,
    public equipos?: IEquipo[]
  ) {}
}
