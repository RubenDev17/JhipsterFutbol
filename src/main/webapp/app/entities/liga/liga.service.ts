import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption, SearchWithPagination } from 'app/shared/util/request-util';
import { ILiga } from 'app/shared/model/liga.model';

type EntityResponseType = HttpResponse<ILiga>;
type EntityArrayResponseType = HttpResponse<ILiga[]>;

@Injectable({ providedIn: 'root' })
export class LigaService {
  public resourceUrl = SERVER_API_URL + 'api/ligas';
  public resourceSearchUrl = SERVER_API_URL + 'api/_search/ligas';

  constructor(protected http: HttpClient) {}

  create(liga: ILiga): Observable<EntityResponseType> {
    return this.http.post<ILiga>(this.resourceUrl, liga, { observe: 'response' });
  }

  update(liga: ILiga): Observable<EntityResponseType> {
    return this.http.put<ILiga>(this.resourceUrl, liga, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ILiga>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ILiga[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ILiga[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }
}
