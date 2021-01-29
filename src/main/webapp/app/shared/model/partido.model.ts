import { Moment } from 'moment';

export interface IPartido {
  id?: number;
  jornada?: number;
  fecha?: Moment;
  rival?: string;
}

export class Partido implements IPartido {
  constructor(public id?: number, public jornada?: number, public fecha?: Moment, public rival?: string) {}
}
