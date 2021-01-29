import { ILiga } from 'app/shared/model/liga.model';

export interface ITemporada {
  id?: number;
  anio?: string;
  ligas?: ILiga[];
}

export class Temporada implements ITemporada {
  constructor(public id?: number, public anio?: string, public ligas?: ILiga[]) {}
}
