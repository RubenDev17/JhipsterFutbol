import { Moment } from 'moment';
import { ILiga } from 'app/shared/model/liga.model';
import { IPresidente } from 'app/shared/model/presidente.model';
import { IDato } from 'app/shared/model/dato.model';

export interface IEquipo {
  id?: number;
  nombre?: string;
  titulos?: number;
  fechaDeFundacion?: Moment;
  liga?: ILiga;
  presidente?: IPresidente;
  datoes?: IDato[];
}

export class Equipo implements IEquipo {
  constructor(
    public id?: number,
    public nombre?: string,
    public titulos?: number,
    public fechaDeFundacion?: Moment,
    public liga?: ILiga,
    public presidente?: IPresidente,
    public datoes?: IDato[]
  ) {}
}
