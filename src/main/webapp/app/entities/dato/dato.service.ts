import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption, SearchWithPagination } from 'app/shared/util/request-util';
import { IDato } from 'app/shared/model/dato.model';

type EntityResponseType = HttpResponse<IDato>;
type EntityArrayResponseType = HttpResponse<IDato[]>;

@Injectable({ providedIn: 'root' })
export class DatoService {
  public resourceUrl = SERVER_API_URL + 'api/datoes';
  public resourceSearchUrl = SERVER_API_URL + 'api/_search/datoes';

  constructor(protected http: HttpClient) {}

  create(dato: IDato): Observable<EntityResponseType> {
    return this.http.post<IDato>(this.resourceUrl, dato, { observe: 'response' });
  }

  update(dato: IDato): Observable<EntityResponseType> {
    return this.http.put<IDato>(this.resourceUrl, dato, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IDato>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IDato[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IDato[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }
}
