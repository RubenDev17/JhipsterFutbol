import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IJugador, Jugador } from 'app/shared/model/jugador.model';
import { JugadorService } from './jugador.service';
import { IEquipo } from 'app/shared/model/equipo.model';
import { EquipoService } from 'app/entities/equipo/equipo.service';

@Component({
  selector: 'jhi-jugador-update',
  templateUrl: './jugador-update.component.html',
})
export class JugadorUpdateComponent implements OnInit {
  isSaving = false;
  equipos: IEquipo[] = [];

  editForm = this.fb.group({
    id: [],
    nombre: [null, [Validators.minLength(3), Validators.maxLength(20)]],
    edad: [],
    equipo: [],
  });

  constructor(
    protected jugadorService: JugadorService,
    protected equipoService: EquipoService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ jugador }) => {
      this.updateForm(jugador);

      this.equipoService.query().subscribe((res: HttpResponse<IEquipo[]>) => (this.equipos = res.body || []));
    });
  }

  updateForm(jugador: IJugador): void {
    this.editForm.patchValue({
      id: jugador.id,
      nombre: jugador.nombre,
      edad: jugador.edad,
      equipo: jugador.equipo,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const jugador = this.createFromForm();
    if (jugador.id !== undefined) {
      this.subscribeToSaveResponse(this.jugadorService.update(jugador));
    } else {
      this.subscribeToSaveResponse(this.jugadorService.create(jugador));
    }
  }

  private createFromForm(): IJugador {
    return {
      ...new Jugador(),
      id: this.editForm.get(['id'])!.value,
      nombre: this.editForm.get(['nombre'])!.value,
      edad: this.editForm.get(['edad'])!.value,
      equipo: this.editForm.get(['equipo'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IJugador>>): void {
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

  trackById(index: number, item: IEquipo): any {
    return item.id;
  }
}
