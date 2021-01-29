import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IDato } from 'app/shared/model/dato.model';

@Component({
  selector: 'jhi-dato-detail',
  templateUrl: './dato-detail.component.html',
})
export class DatoDetailComponent implements OnInit {
  dato: IDato | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ dato }) => (this.dato = dato));
  }

  previousState(): void {
    window.history.back();
  }
}
