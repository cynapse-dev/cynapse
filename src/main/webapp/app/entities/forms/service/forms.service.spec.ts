import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IForms, Forms } from '../forms.model';

import { FormsService } from './forms.service';

describe('Service Tests', () => {
  describe('Forms Service', () => {
    let service: FormsService;
    let httpMock: HttpTestingController;
    let elemDefault: IForms;
    let expectedResult: IForms | IForms[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(FormsService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        name: 'AAAAAAA',
        cynapseId: 'AAAAAAA',
        emailId: 'AAAAAAA',
        phoneNumber: 'AAAAAAA',
        createdDate: currentDate,
        referalCode: 'AAAAAAA',
        formDocumentContentType: 'image/png',
        formDocument: 'AAAAAAA',
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            createdDate: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a Forms', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            createdDate: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            createdDate: currentDate,
          },
          returnedFromService
        );

        service.create(new Forms()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Forms', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            name: 'BBBBBB',
            cynapseId: 'BBBBBB',
            emailId: 'BBBBBB',
            phoneNumber: 'BBBBBB',
            createdDate: currentDate.format(DATE_FORMAT),
            referalCode: 'BBBBBB',
            formDocument: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            createdDate: currentDate,
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Forms', () => {
        const patchObject = Object.assign(
          {
            name: 'BBBBBB',
            cynapseId: 'BBBBBB',
            emailId: 'BBBBBB',
            phoneNumber: 'BBBBBB',
            createdDate: currentDate.format(DATE_FORMAT),
          },
          new Forms()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign(
          {
            createdDate: currentDate,
          },
          returnedFromService
        );

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Forms', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            name: 'BBBBBB',
            cynapseId: 'BBBBBB',
            emailId: 'BBBBBB',
            phoneNumber: 'BBBBBB',
            createdDate: currentDate.format(DATE_FORMAT),
            referalCode: 'BBBBBB',
            formDocument: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            createdDate: currentDate,
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a Forms', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addFormsToCollectionIfMissing', () => {
        it('should add a Forms to an empty array', () => {
          const forms: IForms = { id: 123 };
          expectedResult = service.addFormsToCollectionIfMissing([], forms);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(forms);
        });

        it('should not add a Forms to an array that contains it', () => {
          const forms: IForms = { id: 123 };
          const formsCollection: IForms[] = [
            {
              ...forms,
            },
            { id: 456 },
          ];
          expectedResult = service.addFormsToCollectionIfMissing(formsCollection, forms);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Forms to an array that doesn't contain it", () => {
          const forms: IForms = { id: 123 };
          const formsCollection: IForms[] = [{ id: 456 }];
          expectedResult = service.addFormsToCollectionIfMissing(formsCollection, forms);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(forms);
        });

        it('should add only unique Forms to an array', () => {
          const formsArray: IForms[] = [{ id: 123 }, { id: 456 }, { id: 5237 }];
          const formsCollection: IForms[] = [{ id: 123 }];
          expectedResult = service.addFormsToCollectionIfMissing(formsCollection, ...formsArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const forms: IForms = { id: 123 };
          const forms2: IForms = { id: 456 };
          expectedResult = service.addFormsToCollectionIfMissing([], forms, forms2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(forms);
          expect(expectedResult).toContain(forms2);
        });

        it('should accept null and undefined values', () => {
          const forms: IForms = { id: 123 };
          expectedResult = service.addFormsToCollectionIfMissing([], null, forms, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(forms);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
