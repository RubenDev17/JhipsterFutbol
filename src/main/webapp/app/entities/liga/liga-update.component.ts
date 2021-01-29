import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { ILiga, Liga } from 'app/shared/model/liga.model';
import { LigaService } from './liga.service';
import { ITemporada } from 'app/shared/model/temporada.model';
import { TemporadaService } from 'app/entities/temporada/temporada.service';

@Component({
  selector: 'jhi-liga-update',
  templateUrl: './liga-update.component.html',
})
export class LigaUpdateComponent implements OnInit {
  isSaving = false;
  temporadas: ITemporada[] = [];

  editForm = this.fb.group({
    id: [],
    pais: [null, [Validators.required, Validators.minLength(3), Validators.maxLength(15)]],
    nombre: [null, [Validators.minLength(4), Validators.maxLength(20)]],
    temporada: [],
  });

  constructor(
    protected ligaService: LigaService,
    protected temporadaService: TemporadaService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ liga }) => {
      this.updateForm(liga);

      this.temporadaService.query().subscribe((res: HttpResponse<ITemporada[]>) => (this.temporadas = res.body || []));
    });
  }

  updateForm(liga: ILiga): void {
    this.editForm.patchValue({
      id: liga.id,
      pais: liga.pais,
      nombre: liga.nombre,
      temporada: liga.temporada,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const liga = this.createFromForm();
    if (liga.id !== undefined) {
      this.subscribeToSaveResponse(this.ligaService.update(liga));
    } else {
      this.subscribeToSaveResponse(this.ligaService.create(liga));
    }
  }

  private createFromForm(): ILiga {
    return {
      ...new Liga(),
      id: this.editForm.get(['id'])!.value,
      pais: this.editForm.get(['pais'])!.value,
      nombre: this.editForm.get(['nombre'])!.value,
      temporada: this.editForm.get(['temporada'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ILiga>>): void {
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

  trackById(index: number, item: ITemporada): any {
    return item.id;
  }
}
