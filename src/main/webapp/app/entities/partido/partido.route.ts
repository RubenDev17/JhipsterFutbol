import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IPartido, Partido } from 'app/shared/model/partido.model';
import { PartidoService } from './partido.service';
import { PartidoComponent } from './partido.component';
import { PartidoDetailComponent } from './partido-detail.component';
import { PartidoUpdateComponent } from './partido-update.component';

@Injectable({ providedIn: 'root' })
export class PartidoResolve implements Resolve<IPartido> {
  constructor(private service: PartidoService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPartido> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((partido: HttpResponse<Partido>) => {
          if (partido.body) {
            return of(partido.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Partido());
  }
}

export const partidoRoute: Routes = [
  {
    path: '',
    component: PartidoComponent,
    data: {
      authorities: [Authority.USER],
      defaultSort: 'id,asc',
      pageTitle: 'Partidos',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PartidoDetailComponent,
    resolve: {
      partido: PartidoResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Partidos',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PartidoUpdateComponent,
    resolve: {
      partido: PartidoResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Partidos',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PartidoUpdateComponent,
    resolve: {
      partido: PartidoResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Partidos',
    },
    canActivate: [UserRouteAccessService],
  },
];
