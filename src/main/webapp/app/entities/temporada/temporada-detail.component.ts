import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ITemporada } from 'app/shared/model/temporada.model';

@Component({
  selector: 'jhi-temporada-detail',
  templateUrl: './temporada-detail.component.html',
})
export class TemporadaDetailComponent implements OnInit {
  temporada: ITemporada | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ temporada }) => (this.temporada = temporada));
  }

  previousState(): void {
    window.history.back();
  }
}
