import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'temporada',
        loadChildren: () => import('./temporada/temporada.module').then(m => m.FutbolTemporadaModule),
      },
      {
        path: 'liga',
        loadChildren: () => import('./liga/liga.module').then(m => m.FutbolLigaModule),
      },
      {
        path: 'equipo',
        loadChildren: () => import('./equipo/equipo.module').then(m => m.FutbolEquipoModule),
      },
      {
        path: 'presidente',
        loadChildren: () => import('./presidente/presidente.module').then(m => m.FutbolPresidenteModule),
      },
      {
        path: 'dato',
        loadChildren: () => import('./dato/dato.module').then(m => m.FutbolDatoModule),
      },
      {
        path: 'jugador',
        loadChildren: () => import('./jugador/jugador.module').then(m => m.FutbolJugadorModule),
      },
      {
        path: 'partido',
        loadChildren: () => import('./partido/partido.module').then(m => m.FutbolPartidoModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class FutbolEntityModule {}
