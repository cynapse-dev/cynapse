import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'forms',
        data: { pageTitle: 'cynapseTechApp.forms.home.title' },
        loadChildren: () => import('./forms/forms.module').then(m => m.FormsModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
