import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IJugador } from 'app/shared/model/jugador.model';
import { JugadorService } from './jugador.service';

@Component({
  templateUrl: './jugador-delete-dialog.component.html',
})
export class JugadorDeleteDialogComponent {
  jugador?: IJugador;

  constructor(protected jugadorService: JugadorService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.jugadorService.delete(id).subscribe(() => {
      this.eventManager.broadcast('jugadorListModification');
      this.activeModal.close();
    });
  }
}
