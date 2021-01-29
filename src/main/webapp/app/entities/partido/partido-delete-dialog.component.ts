import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IPartido } from 'app/shared/model/partido.model';
import { PartidoService } from './partido.service';

@Component({
  templateUrl: './partido-delete-dialog.component.html',
})
export class PartidoDeleteDialogComponent {
  partido?: IPartido;

  constructor(protected partidoService: PartidoService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.partidoService.delete(id).subscribe(() => {
      this.eventManager.broadcast('partidoListModification');
      this.activeModal.close();
    });
  }
}
