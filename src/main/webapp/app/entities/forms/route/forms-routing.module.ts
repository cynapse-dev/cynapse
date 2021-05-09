import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { FormsComponent } from '../list/forms.component';
import { FormsDetailComponent } from '../detail/forms-detail.component';
import { FormsUpdateComponent } from '../update/forms-update.component';
import { FormsRoutingResolveService } from './forms-routing-resolve.service';

const formsRoute: Routes = [
  {
    path: '',
    component: FormsComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: FormsDetailComponent,
    resolve: {
      forms: FormsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: FormsUpdateComponent,
    resolve: {
      forms: FormsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: FormsUpdateComponent,
    resolve: {
      forms: FormsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(formsRoute)],
  exports: [RouterModule],
})
export class FormsRoutingModule {}
