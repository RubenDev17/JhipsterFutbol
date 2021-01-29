import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IDato, Dato } from 'app/shared/model/dato.model';
import { DatoService } from './dato.service';
import { DatoComponent } from './dato.component';
import { DatoDetailComponent } from './dato-detail.component';
import { DatoUpdateComponent } from './dato-update.component';

@Injectable({ providedIn: 'root' })
export class DatoResolve implements Resolve<IDato> {
  constructor(private service: DatoService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IDato> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((dato: HttpResponse<Dato>) => {
          if (dato.body) {
            return of(dato.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Dato());
  }
}

export const datoRoute: Routes = [
  {
    path: '',
    component: DatoComponent,
    data: {
      authorities: [Authority.USER],
      defaultSort: 'id,asc',
      pageTitle: 'Datoes',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: DatoDetailComponent,
    resolve: {
      dato: DatoResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Datoes',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: DatoUpdateComponent,
    resolve: {
      dato: DatoResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Datoes',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: DatoUpdateComponent,
    resolve: {
      dato: DatoResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Datoes',
    },
    canActivate: [UserRouteAccessService],
  },
];
