import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { FormsComponent } from './list/forms.component';
import { FormsDetailComponent } from './detail/forms-detail.component';
import { FormsUpdateComponent } from './update/forms-update.component';
import { FormsDeleteDialogComponent } from './delete/forms-delete-dialog.component';
import { FormsRoutingModule } from './route/forms-routing.module';

@NgModule({
  imports: [SharedModule, FormsRoutingModule],
  declarations: [FormsComponent, FormsDetailComponent, FormsUpdateComponent, FormsDeleteDialogComponent],
  entryComponents: [FormsDeleteDialogComponent],
})
export class FormsModule {}
