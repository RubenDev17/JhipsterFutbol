export interface ILiga {
  id?: number;
  pais?: string;
  nombre?: string;
}

export class Liga implements ILiga {
  constructor(public id?: number, public pais?: string, public nombre?: string) {}
}
