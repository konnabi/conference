import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IPresentation } from 'app/shared/model/presentation.model';

type EntityResponseType = HttpResponse<IPresentation>;
type EntityArrayResponseType = HttpResponse<IPresentation[]>;

@Injectable({ providedIn: 'root' })
export class PresentationService {
  public resourceUrl = SERVER_API_URL + 'api/presentations';

  constructor(protected http: HttpClient) {}

  create(presentation: IPresentation): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(presentation);
    return this.http
      .post<IPresentation>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(presentation: IPresentation): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(presentation);
    return this.http
      .put<IPresentation>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IPresentation>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IPresentation[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(presentation: IPresentation): IPresentation {
    const copy: IPresentation = Object.assign({}, presentation, {
      presentationDate:
        presentation.presentationDate != null && presentation.presentationDate.isValid() ? presentation.presentationDate.toJSON() : null
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.presentationDate = res.body.presentationDate != null ? moment(res.body.presentationDate) : null;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((presentation: IPresentation) => {
        presentation.presentationDate = presentation.presentationDate != null ? moment(presentation.presentationDate) : null;
      });
    }
    return res;
  }
}
