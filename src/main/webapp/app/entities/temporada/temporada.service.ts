import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption, SearchWithPagination } from 'app/shared/util/request-util';
import { ITemporada } from 'app/shared/model/temporada.model';

type EntityResponseType = HttpResponse<ITemporada>;
type EntityArrayResponseType = HttpResponse<ITemporada[]>;

@Injectable({ providedIn: 'root' })
export class TemporadaService {
  public resourceUrl = SERVER_API_URL + 'api/temporadas';
  public resourceSearchUrl = SERVER_API_URL + 'api/_search/temporadas';

  constructor(protected http: HttpClient) {}

  create(temporada: ITemporada): Observable<EntityResponseType> {
    return this.http.post<ITemporada>(this.resourceUrl, temporada, { observe: 'response' });
  }

  update(temporada: ITemporada): Observable<EntityResponseType> {
    return this.http.put<ITemporada>(this.resourceUrl, temporada, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ITemporada>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ITemporada[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ITemporada[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }
}
