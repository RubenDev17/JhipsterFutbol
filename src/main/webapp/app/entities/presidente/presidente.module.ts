import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { FutbolSharedModule } from 'app/shared/shared.module';
import { PresidenteComponent } from './presidente.component';
import { PresidenteDetailComponent } from './presidente-detail.component';
import { PresidenteUpdateComponent } from './presidente-update.component';
import { PresidenteDeleteDialogComponent } from './presidente-delete-dialog.component';
import { presidenteRoute } from './presidente.route';

@NgModule({
  imports: [FutbolSharedModule, RouterModule.forChild(presidenteRoute)],
  declarations: [PresidenteComponent, PresidenteDetailComponent, PresidenteUpdateComponent, PresidenteDeleteDialogComponent],
  entryComponents: [PresidenteDeleteDialogComponent],
})
export class FutbolPresidenteModule {}
