import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';

import { IPartido, Partido } from 'app/shared/model/partido.model';
import { PartidoService } from './partido.service';
import { IJugador } from 'app/shared/model/jugador.model';
import { JugadorService } from 'app/entities/jugador/jugador.service';

@Component({
  selector: 'jhi-partido-update',
  templateUrl: './partido-update.component.html',
})
export class PartidoUpdateComponent implements OnInit {
  isSaving = false;
  jugadors: IJugador[] = [];

  editForm = this.fb.group({
    id: [],
    jornada: [],
    fecha: [],
    rival: [null, [Validators.minLength(3), Validators.maxLength(20)]],
    jugadors: [],
  });

  constructor(
    protected partidoService: PartidoService,
    protected jugadorService: JugadorService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ partido }) => {
      if (!partido.id) {
        const today = moment().startOf('day');
        partido.fecha = today;
      }

      this.updateForm(partido);

      this.jugadorService.query().subscribe((res: HttpResponse<IJugador[]>) => (this.jugadors = res.body || []));
    });
  }

  updateForm(partido: IPartido): void {
    this.editForm.patchValue({
      id: partido.id,
      jornada: partido.jornada,
      fecha: partido.fecha ? partido.fecha.format(DATE_TIME_FORMAT) : null,
      rival: partido.rival,
      jugadors: partido.jugadors,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const partido = this.createFromForm();
    if (partido.id !== undefined) {
      this.subscribeToSaveResponse(this.partidoService.update(partido));
    } else {
      this.subscribeToSaveResponse(this.partidoService.create(partido));
    }
  }

  private createFromForm(): IPartido {
    return {
      ...new Partido(),
      id: this.editForm.get(['id'])!.value,
      jornada: this.editForm.get(['jornada'])!.value,
      fecha: this.editForm.get(['fecha'])!.value ? moment(this.editForm.get(['fecha'])!.value, DATE_TIME_FORMAT) : undefined,
      rival: this.editForm.get(['rival'])!.value,
      jugadors: this.editForm.get(['jugadors'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPartido>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }

  trackById(index: number, item: IJugador): any {
    return item.id;
  }

  getSelected(selectedVals: IJugador[], option: IJugador): IJugador {
    if (selectedVals) {
      for (let i = 0; i < selectedVals.length; i++) {
        if (option.id === selectedVals[i].id) {
          return selectedVals[i];
        }
      }
    }
    return option;
  }
}
