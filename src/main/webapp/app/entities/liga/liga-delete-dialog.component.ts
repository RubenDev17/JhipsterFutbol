import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ILiga } from 'app/shared/model/liga.model';
import { LigaService } from './liga.service';

@Component({
  templateUrl: './liga-delete-dialog.component.html',
})
export class LigaDeleteDialogComponent {
  liga?: ILiga;

  constructor(protected ligaService: LigaService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.ligaService.delete(id).subscribe(() => {
      this.eventManager.broadcast('ligaListModification');
      this.activeModal.close();
    });
  }
}
