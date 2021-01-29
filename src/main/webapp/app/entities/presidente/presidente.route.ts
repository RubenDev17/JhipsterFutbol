import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IPresidente, Presidente } from 'app/shared/model/presidente.model';
import { PresidenteService } from './presidente.service';
import { PresidenteComponent } from './presidente.component';
import { PresidenteDetailComponent } from './presidente-detail.component';
import { PresidenteUpdateComponent } from './presidente-update.component';

@Injectable({ providedIn: 'root' })
export class PresidenteResolve implements Resolve<IPresidente> {
  constructor(private service: PresidenteService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPresidente> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((presidente: HttpResponse<Presidente>) => {
          if (presidente.body) {
            return of(presidente.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Presidente());
  }
}

export const presidenteRoute: Routes = [
  {
    path: '',
    component: PresidenteComponent,
    data: {
      authorities: [Authority.USER],
      defaultSort: 'id,asc',
      pageTitle: 'Presidentes',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PresidenteDetailComponent,
    resolve: {
      presidente: PresidenteResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Presidentes',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PresidenteUpdateComponent,
    resolve: {
      presidente: PresidenteResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Presidentes',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PresidenteUpdateComponent,
    resolve: {
      presidente: PresidenteResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Presidentes',
    },
    canActivate: [UserRouteAccessService],
  },
];
