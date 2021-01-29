import { IEquipo } from 'app/shared/model/equipo.model';

export interface IDato {
  id?: number;
  jornada?: number;
  rival?: string;
  resultado?: string;
  numeroDeGoles?: number;
  corner?: number;
  faltas?: number;
  equipo?: IEquipo;
}

export class Dato implements IDato {
  constructor(
    public id?: number,
    public jornada?: number,
    public rival?: string,
    public resultado?: string,
    public numeroDeGoles?: number,
    public corner?: number,
    public faltas?: number,
    public equipo?: IEquipo
  ) {}
}
