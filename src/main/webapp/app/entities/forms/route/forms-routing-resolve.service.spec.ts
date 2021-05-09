jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IForms, Forms } from '../forms.model';
import { FormsService } from '../service/forms.service';

import { FormsRoutingResolveService } from './forms-routing-resolve.service';

describe('Service Tests', () => {
  describe('Forms routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: FormsRoutingResolveService;
    let service: FormsService;
    let resultForms: IForms | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(FormsRoutingResolveService);
      service = TestBed.inject(FormsService);
      resultForms = undefined;
    });

    describe('resolve', () => {
      it('should return IForms returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultForms = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultForms).toEqual({ id: 123 });
      });

      it('should return new IForms if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultForms = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultForms).toEqual(new Forms());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultForms = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultForms).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
