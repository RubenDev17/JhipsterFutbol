import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption, SearchWithPagination } from 'app/shared/util/request-util';
import { IPresidente } from 'app/shared/model/presidente.model';

type EntityResponseType = HttpResponse<IPresidente>;
type EntityArrayResponseType = HttpResponse<IPresidente[]>;

@Injectable({ providedIn: 'root' })
export class PresidenteService {
  public resourceUrl = SERVER_API_URL + 'api/presidentes';
  public resourceSearchUrl = SERVER_API_URL + 'api/_search/presidentes';

  constructor(protected http: HttpClient) {}

  create(presidente: IPresidente): Observable<EntityResponseType> {
    return this.http.post<IPresidente>(this.resourceUrl, presidente, { observe: 'response' });
  }

  update(presidente: IPresidente): Observable<EntityResponseType> {
    return this.http.put<IPresidente>(this.resourceUrl, presidente, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IPresidente>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPresidente[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPresidente[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }
}
