import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IForms, getFormsIdentifier } from '../forms.model';

export type EntityResponseType = HttpResponse<IForms>;
export type EntityArrayResponseType = HttpResponse<IForms[]>;

@Injectable({ providedIn: 'root' })
export class FormsService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/forms');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(forms: IForms): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(forms);
    return this.http
      .post<IForms>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(forms: IForms): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(forms);
    return this.http
      .put<IForms>(`${this.resourceUrl}/${getFormsIdentifier(forms) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(forms: IForms): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(forms);
    return this.http
      .patch<IForms>(`${this.resourceUrl}/${getFormsIdentifier(forms) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IForms>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IForms[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addFormsToCollectionIfMissing(formsCollection: IForms[], ...formsToCheck: (IForms | null | undefined)[]): IForms[] {
    const forms: IForms[] = formsToCheck.filter(isPresent);
    if (forms.length > 0) {
      const formsCollectionIdentifiers = formsCollection.map(formsItem => getFormsIdentifier(formsItem)!);
      const formsToAdd = forms.filter(formsItem => {
        const formsIdentifier = getFormsIdentifier(formsItem);
        if (formsIdentifier == null || formsCollectionIdentifiers.includes(formsIdentifier)) {
          return false;
        }
        formsCollectionIdentifiers.push(formsIdentifier);
        return true;
      });
      return [...formsToAdd, ...formsCollection];
    }
    return formsCollection;
  }

  protected convertDateFromClient(forms: IForms): IForms {
    return Object.assign({}, forms, {
      createdDate: forms.createdDate?.isValid() ? forms.createdDate.format(DATE_FORMAT) : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.createdDate = res.body.createdDate ? dayjs(res.body.createdDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((forms: IForms) => {
        forms.createdDate = forms.createdDate ? dayjs(forms.createdDate) : undefined;
      });
    }
    return res;
  }
}
