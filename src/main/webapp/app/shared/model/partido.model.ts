import { Moment } from 'moment';
import { IJugador } from 'app/shared/model/jugador.model';

export interface IPartido {
  id?: number;
  jornada?: number;
  fecha?: Moment;
  rival?: string;
  jugadors?: IJugador[];
}

export class Partido implements IPartido {
  constructor(public id?: number, public jornada?: number, public fecha?: Moment, public rival?: string, public jugadors?: IJugador[]) {}
}
