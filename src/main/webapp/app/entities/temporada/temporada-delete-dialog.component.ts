import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ITemporada } from 'app/shared/model/temporada.model';
import { TemporadaService } from './temporada.service';

@Component({
  templateUrl: './temporada-delete-dialog.component.html',
})
export class TemporadaDeleteDialogComponent {
  temporada?: ITemporada;

  constructor(protected temporadaService: TemporadaService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.temporadaService.delete(id).subscribe(() => {
      this.eventManager.broadcast('temporadaListModification');
      this.activeModal.close();
    });
  }
}
