export interface IPresidente {
  id?: number;
  nombre?: string;
  aniosEnPresidencia?: number;
}

export class Presidente implements IPresidente {
  constructor(public id?: number, public nombre?: string, public aniosEnPresidencia?: number) {}
}
