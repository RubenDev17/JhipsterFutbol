import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IPresidente } from 'app/shared/model/presidente.model';
import { PresidenteService } from './presidente.service';

@Component({
  templateUrl: './presidente-delete-dialog.component.html',
})
export class PresidenteDeleteDialogComponent {
  presidente?: IPresidente;

  constructor(
    protected presidenteService: PresidenteService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.presidenteService.delete(id).subscribe(() => {
      this.eventManager.broadcast('presidenteListModification');
      this.activeModal.close();
    });
  }
}
