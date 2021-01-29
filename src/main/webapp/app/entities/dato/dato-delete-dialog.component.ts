import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IDato } from 'app/shared/model/dato.model';
import { DatoService } from './dato.service';

@Component({
  templateUrl: './dato-delete-dialog.component.html',
})
export class DatoDeleteDialogComponent {
  dato?: IDato;

  constructor(protected datoService: DatoService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.datoService.delete(id).subscribe(() => {
      this.eventManager.broadcast('datoListModification');
      this.activeModal.close();
    });
  }
}
