import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPresidente } from 'app/shared/model/presidente.model';

@Component({
  selector: 'jhi-presidente-detail',
  templateUrl: './presidente-detail.component.html',
})
export class PresidenteDetailComponent implements OnInit {
  presidente: IPresidente | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ presidente }) => (this.presidente = presidente));
  }

  previousState(): void {
    window.history.back();
  }
}
