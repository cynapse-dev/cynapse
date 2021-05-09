import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IForms } from '../forms.model';
import { FormsService } from '../service/forms.service';
import { FormsDeleteDialogComponent } from '../delete/forms-delete-dialog.component';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-forms',
  templateUrl: './forms.component.html',
})
export class FormsComponent implements OnInit {
  forms?: IForms[];
  isLoading = false;

  constructor(protected formsService: FormsService, protected dataUtils: DataUtils, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.formsService.query().subscribe(
      (res: HttpResponse<IForms[]>) => {
        this.isLoading = false;
        this.forms = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IForms): number {
    return item.id!;
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    return this.dataUtils.openFile(base64String, contentType);
  }

  delete(forms: IForms): void {
    const modalRef = this.modalService.open(FormsDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.forms = forms;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
