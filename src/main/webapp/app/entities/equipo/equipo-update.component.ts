import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';

import { IEquipo, Equipo } from 'app/shared/model/equipo.model';
import { EquipoService } from './equipo.service';
import { ILiga } from 'app/shared/model/liga.model';
import { LigaService } from 'app/entities/liga/liga.service';

@Component({
  selector: 'jhi-equipo-update',
  templateUrl: './equipo-update.component.html',
})
export class EquipoUpdateComponent implements OnInit {
  isSaving = false;
  ligas: ILiga[] = [];

  editForm = this.fb.group({
    id: [],
    nombre: [null, [Validators.required, Validators.minLength(2), Validators.maxLength(20)]],
    titulos: [],
    fechaDeFundacion: [],
    liga: [],
  });

  constructor(
    protected equipoService: EquipoService,
    protected ligaService: LigaService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ equipo }) => {
      if (!equipo.id) {
        const today = moment().startOf('day');
        equipo.fechaDeFundacion = today;
      }

      this.updateForm(equipo);

      this.ligaService.query().subscribe((res: HttpResponse<ILiga[]>) => (this.ligas = res.body || []));
    });
  }

  updateForm(equipo: IEquipo): void {
    this.editForm.patchValue({
      id: equipo.id,
      nombre: equipo.nombre,
      titulos: equipo.titulos,
      fechaDeFundacion: equipo.fechaDeFundacion ? equipo.fechaDeFundacion.format(DATE_TIME_FORMAT) : null,
      liga: equipo.liga,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const equipo = this.createFromForm();
    if (equipo.id !== undefined) {
      this.subscribeToSaveResponse(this.equipoService.update(equipo));
    } else {
      this.subscribeToSaveResponse(this.equipoService.create(equipo));
    }
  }

  private createFromForm(): IEquipo {
    return {
      ...new Equipo(),
      id: this.editForm.get(['id'])!.value,
      nombre: this.editForm.get(['nombre'])!.value,
      titulos: this.editForm.get(['titulos'])!.value,
      fechaDeFundacion: this.editForm.get(['fechaDeFundacion'])!.value
        ? moment(this.editForm.get(['fechaDeFundacion'])!.value, DATE_TIME_FORMAT)
        : undefined,
      liga: this.editForm.get(['liga'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEquipo>>): void {
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

  trackById(index: number, item: ILiga): any {
    return item.id;
  }
}
