import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { ITemporada, Temporada } from 'app/shared/model/temporada.model';
import { TemporadaService } from './temporada.service';

@Component({
  selector: 'jhi-temporada-update',
  templateUrl: './temporada-update.component.html',
})
export class TemporadaUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    anio: [null, [Validators.required, Validators.minLength(2), Validators.maxLength(10)]],
  });

  constructor(protected temporadaService: TemporadaService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ temporada }) => {
      this.updateForm(temporada);
    });
  }

  updateForm(temporada: ITemporada): void {
    this.editForm.patchValue({
      id: temporada.id,
      anio: temporada.anio,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const temporada = this.createFromForm();
    if (temporada.id !== undefined) {
      this.subscribeToSaveResponse(this.temporadaService.update(temporada));
    } else {
      this.subscribeToSaveResponse(this.temporadaService.create(temporada));
    }
  }

  private createFromForm(): ITemporada {
    return {
      ...new Temporada(),
      id: this.editForm.get(['id'])!.value,
      anio: this.editForm.get(['anio'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITemporada>>): void {
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
}
