import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { FutbolSharedModule } from 'app/shared/shared.module';
import { JugadorComponent } from './jugador.component';
import { JugadorDetailComponent } from './jugador-detail.component';
import { JugadorUpdateComponent } from './jugador-update.component';
import { JugadorDeleteDialogComponent } from './jugador-delete-dialog.component';
import { jugadorRoute } from './jugador.route';

@NgModule({
  imports: [FutbolSharedModule, RouterModule.forChild(jugadorRoute)],
  declarations: [JugadorComponent, JugadorDetailComponent, JugadorUpdateComponent, JugadorDeleteDialogComponent],
  entryComponents: [JugadorDeleteDialogComponent],
})
export class FutbolJugadorModule {}
