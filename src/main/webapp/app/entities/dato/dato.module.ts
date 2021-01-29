import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { FutbolSharedModule } from 'app/shared/shared.module';
import { DatoComponent } from './dato.component';
import { DatoDetailComponent } from './dato-detail.component';
import { DatoUpdateComponent } from './dato-update.component';
import { DatoDeleteDialogComponent } from './dato-delete-dialog.component';
import { datoRoute } from './dato.route';

@NgModule({
  imports: [FutbolSharedModule, RouterModule.forChild(datoRoute)],
  declarations: [DatoComponent, DatoDetailComponent, DatoUpdateComponent, DatoDeleteDialogComponent],
  entryComponents: [DatoDeleteDialogComponent],
})
export class FutbolDatoModule {}
