import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption, SearchWithPagination } from 'app/shared/util/request-util';
import { IEquipo } from 'app/shared/model/equipo.model';

type EntityResponseType = HttpResponse<IEquipo>;
type EntityArrayResponseType = HttpResponse<IEquipo[]>;

@Injectable({ providedIn: 'root' })
export class EquipoService {
  public resourceUrl = SERVER_API_URL + 'api/equipos';
  public resourceSearchUrl = SERVER_API_URL + 'api/_search/equipos';

  constructor(protected http: HttpClient) {}

  create(equipo: IEquipo): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(equipo);
    return this.http
      .post<IEquipo>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(equipo: IEquipo): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(equipo);
    return this.http
      .put<IEquipo>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IEquipo>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IEquipo[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IEquipo[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  protected convertDateFromClient(equipo: IEquipo): IEquipo {
    const copy: IEquipo = Object.assign({}, equipo, {
      fechaDeFundacion: equipo.fechaDeFundacion && equipo.fechaDeFundacion.isValid() ? equipo.fechaDeFundacion.toJSON() : undefined,
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.fechaDeFundacion = res.body.fechaDeFundacion ? moment(res.body.fechaDeFundacion) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((equipo: IEquipo) => {
        equipo.fechaDeFundacion = equipo.fechaDeFundacion ? moment(equipo.fechaDeFundacion) : undefined;
      });
    }
    return res;
  }
}
