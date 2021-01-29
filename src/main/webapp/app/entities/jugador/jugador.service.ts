import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption, SearchWithPagination } from 'app/shared/util/request-util';
import { IJugador } from 'app/shared/model/jugador.model';

type EntityResponseType = HttpResponse<IJugador>;
type EntityArrayResponseType = HttpResponse<IJugador[]>;

@Injectable({ providedIn: 'root' })
export class JugadorService {
  public resourceUrl = SERVER_API_URL + 'api/jugadors';
  public resourceSearchUrl = SERVER_API_URL + 'api/_search/jugadors';

  constructor(protected http: HttpClient) {}

  create(jugador: IJugador): Observable<EntityResponseType> {
    return this.http.post<IJugador>(this.resourceUrl, jugador, { observe: 'response' });
  }

  update(jugador: IJugador): Observable<EntityResponseType> {
    return this.http.put<IJugador>(this.resourceUrl, jugador, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IJugador>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IJugador[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IJugador[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }
}
