import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPartido } from 'app/shared/model/partido.model';

@Component({
  selector: 'jhi-partido-detail',
  templateUrl: './partido-detail.component.html',
})
export class PartidoDetailComponent implements OnInit {
  partido: IPartido | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ partido }) => (this.partido = partido));
  }

  previousState(): void {
    window.history.back();
  }
}
