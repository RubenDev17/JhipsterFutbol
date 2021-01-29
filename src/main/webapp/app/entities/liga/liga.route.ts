import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { ILiga, Liga } from 'app/shared/model/liga.model';
import { LigaService } from './liga.service';
import { LigaComponent } from './liga.component';
import { LigaDetailComponent } from './liga-detail.component';
import { LigaUpdateComponent } from './liga-update.component';

@Injectable({ providedIn: 'root' })
export class LigaResolve implements Resolve<ILiga> {
  constructor(private service: LigaService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ILiga> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((liga: HttpResponse<Liga>) => {
          if (liga.body) {
            return of(liga.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Liga());
  }
}

export const ligaRoute: Routes = [
  {
    path: '',
    component: LigaComponent,
    data: {
      authorities: [Authority.USER],
      defaultSort: 'id,asc',
      pageTitle: 'Ligas',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: LigaDetailComponent,
    resolve: {
      liga: LigaResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Ligas',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: LigaUpdateComponent,
    resolve: {
      liga: LigaResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Ligas',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: LigaUpdateComponent,
    resolve: {
      liga: LigaResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Ligas',
    },
    canActivate: [UserRouteAccessService],
  },
];
