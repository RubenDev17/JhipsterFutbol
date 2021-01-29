import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IDato, Dato } from 'app/shared/model/dato.model';
import { DatoService } from './dato.service';

@Component({
  selector: 'jhi-dato-update',
  templateUrl: './dato-update.component.html',
})
export class DatoUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    jornada: [],
    rival: [null, [Validators.required, Validators.minLength(2), Validators.maxLength(20)]],
    resultado: [null, [Validators.minLength(1), Validators.maxLength(10)]],
    numeroDeGoles: [],
    corner: [],
    faltas: [],
  });

  constructor(protected datoService: DatoService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ dato }) => {
      this.updateForm(dato);
    });
  }

  updateForm(dato: IDato): void {
    this.editForm.patchValue({
      id: dato.id,
      jornada: dato.jornada,
      rival: dato.rival,
      resultado: dato.resultado,
      numeroDeGoles: dato.numeroDeGoles,
      corner: dato.corner,
      faltas: dato.faltas,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const dato = this.createFromForm();
    if (dato.id !== undefined) {
      this.subscribeToSaveResponse(this.datoService.update(dato));
    } else {
      this.subscribeToSaveResponse(this.datoService.create(dato));
    }
  }

  private createFromForm(): IDato {
    return {
      ...new Dato(),
      id: this.editForm.get(['id'])!.value,
      jornada: this.editForm.get(['jornada'])!.value,
      rival: this.editForm.get(['rival'])!.value,
      resultado: this.editForm.get(['resultado'])!.value,
      numeroDeGoles: this.editForm.get(['numeroDeGoles'])!.value,
      corner: this.editForm.get(['corner'])!.value,
      faltas: this.editForm.get(['faltas'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDato>>): void {
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
