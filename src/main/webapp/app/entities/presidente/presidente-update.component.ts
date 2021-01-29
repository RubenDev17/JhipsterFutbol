import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { IPresidente, Presidente } from 'app/shared/model/presidente.model';
import { PresidenteService } from './presidente.service';
import { IEquipo } from 'app/shared/model/equipo.model';
import { EquipoService } from 'app/entities/equipo/equipo.service';

@Component({
  selector: 'jhi-presidente-update',
  templateUrl: './presidente-update.component.html',
})
export class PresidenteUpdateComponent implements OnInit {
  isSaving = false;
  equipos: IEquipo[] = [];

  editForm = this.fb.group({
    id: [],
    nombre: [null, [Validators.required, Validators.minLength(2), Validators.maxLength(20)]],
    aniosEnPresidencia: [],
    equipo: [],
  });

  constructor(
    protected presidenteService: PresidenteService,
    protected equipoService: EquipoService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ presidente }) => {
      this.updateForm(presidente);

      this.equipoService
        .query({ filter: 'presidente-is-null' })
        .pipe(
          map((res: HttpResponse<IEquipo[]>) => {
            return res.body || [];
          })
        )
        .subscribe((resBody: IEquipo[]) => {
          if (!presidente.equipo || !presidente.equipo.id) {
            this.equipos = resBody;
          } else {
            this.equipoService
              .find(presidente.equipo.id)
              .pipe(
                map((subRes: HttpResponse<IEquipo>) => {
                  return subRes.body ? [subRes.body].concat(resBody) : resBody;
                })
              )
              .subscribe((concatRes: IEquipo[]) => (this.equipos = concatRes));
          }
        });
    });
  }

  updateForm(presidente: IPresidente): void {
    this.editForm.patchValue({
      id: presidente.id,
      nombre: presidente.nombre,
      aniosEnPresidencia: presidente.aniosEnPresidencia,
      equipo: presidente.equipo,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const presidente = this.createFromForm();
    if (presidente.id !== undefined) {
      this.subscribeToSaveResponse(this.presidenteService.update(presidente));
    } else {
      this.subscribeToSaveResponse(this.presidenteService.create(presidente));
    }
  }

  private createFromForm(): IPresidente {
    return {
      ...new Presidente(),
      id: this.editForm.get(['id'])!.value,
      nombre: this.editForm.get(['nombre'])!.value,
      aniosEnPresidencia: this.editForm.get(['aniosEnPresidencia'])!.value,
      equipo: this.editForm.get(['equipo'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPresidente>>): void {
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
