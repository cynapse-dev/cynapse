import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IForms, Forms } from '../forms.model';
import { FormsService } from '../service/forms.service';

@Injectable({ providedIn: 'root' })
export class FormsRoutingResolveService implements Resolve<IForms> {
  constructor(protected service: FormsService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IForms> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((forms: HttpResponse<Forms>) => {
          if (forms.body) {
            return of(forms.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Forms());
  }
}
