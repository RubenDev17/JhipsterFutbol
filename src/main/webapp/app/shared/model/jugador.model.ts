export interface IJugador {
  id?: number;
  nombre?: string;
  edad?: number;
}

export class Jugador implements IJugador {
  constructor(public id?: number, public nombre?: string, public edad?: number) {}
}
