import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IForms } from '../forms.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-forms-detail',
  templateUrl: './forms-detail.component.html',
})
export class FormsDetailComponent implements OnInit {
  forms: IForms | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ forms }) => {
      this.forms = forms;
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  previousState(): void {
    window.history.back();
  }
}
