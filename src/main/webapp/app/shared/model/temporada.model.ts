export interface ITemporada {
  id?: number;
  anio?: string;
}

export class Temporada implements ITemporada {
  constructor(public id?: number, public anio?: string) {}
}
