import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { ITemporada, Temporada } from 'app/shared/model/temporada.model';
import { TemporadaService } from './temporada.service';
import { TemporadaComponent } from './temporada.component';
import { TemporadaDetailComponent } from './temporada-detail.component';
import { TemporadaUpdateComponent } from './temporada-update.component';

@Injectable({ providedIn: 'root' })
export class TemporadaResolve implements Resolve<ITemporada> {
  constructor(private service: TemporadaService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ITemporada> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((temporada: HttpResponse<Temporada>) => {
          if (temporada.body) {
            return of(temporada.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Temporada());
  }
}

export const temporadaRoute: Routes = [
  {
    path: '',
    component: TemporadaComponent,
    data: {
      authorities: [Authority.USER],
      defaultSort: 'id,asc',
      pageTitle: 'Temporadas',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: TemporadaDetailComponent,
    resolve: {
      temporada: TemporadaResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Temporadas',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: TemporadaUpdateComponent,
    resolve: {
      temporada: TemporadaResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Temporadas',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: TemporadaUpdateComponent,
    resolve: {
      temporada: TemporadaResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Temporadas',
    },
    canActivate: [UserRouteAccessService],
  },
];
