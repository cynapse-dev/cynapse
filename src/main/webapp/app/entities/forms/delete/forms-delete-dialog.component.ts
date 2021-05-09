import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IForms } from '../forms.model';
import { FormsService } from '../service/forms.service';

@Component({
  templateUrl: './forms-delete-dialog.component.html',
})
export class FormsDeleteDialogComponent {
  forms?: IForms;

  constructor(protected formsService: FormsService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.formsService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
