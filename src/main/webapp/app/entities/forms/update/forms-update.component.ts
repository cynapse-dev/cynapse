import { Component, OnInit, ElementRef } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IForms, Forms } from '../forms.model';
import { FormsService } from '../service/forms.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';

@Component({
  selector: 'jhi-forms-update',
  templateUrl: './forms-update.component.html',
})
export class FormsUpdateComponent implements OnInit {
  isSaving = false;

  usersSharedCollection: IUser[] = [];

  editForm = this.fb.group({
    id: [],
    name: [],
    cynapseId: [],
    emailId: [],
    phoneNumber: [],
    createdDate: [],
    referalCode: [],
    formDocument: [],
    formDocumentContentType: [],
    user: [],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected formsService: FormsService,
    protected userService: UserService,
    protected elementRef: ElementRef,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ forms }) => {
      this.updateForm(forms);

      this.loadRelationshipsOptions();
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe({
      error: (err: FileLoadError) =>
        this.eventManager.broadcast(
          new EventWithContent<AlertError>('cynapseTechApp.error', { ...err, key: 'error.file.' + err.key })
        ),
    });
  }

  clearInputImage(field: string, fieldContentType: string, idInput: string): void {
    this.editForm.patchValue({
      [field]: null,
      [fieldContentType]: null,
    });
    if (idInput && this.elementRef.nativeElement.querySelector('#' + idInput)) {
      this.elementRef.nativeElement.querySelector('#' + idInput).value = null;
    }
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const forms = this.createFromForm();
    if (forms.id !== undefined) {
      this.subscribeToSaveResponse(this.formsService.update(forms));
    } else {
      this.subscribeToSaveResponse(this.formsService.create(forms));
    }
  }

  trackUserById(index: number, item: IUser): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IForms>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(forms: IForms): void {
    this.editForm.patchValue({
      id: forms.id,
      name: forms.name,
      cynapseId: forms.cynapseId,
      emailId: forms.emailId,
      phoneNumber: forms.phoneNumber,
      createdDate: forms.createdDate,
      referalCode: forms.referalCode,
      formDocument: forms.formDocument,
      formDocumentContentType: forms.formDocumentContentType,
      user: forms.user,
    });

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing(this.usersSharedCollection, forms.user);
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing(users, this.editForm.get('user')!.value)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));
  }

  protected createFromForm(): IForms {
    return {
      ...new Forms(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      cynapseId: this.editForm.get(['cynapseId'])!.value,
      emailId: this.editForm.get(['emailId'])!.value,
      phoneNumber: this.editForm.get(['phoneNumber'])!.value,
      createdDate: this.editForm.get(['createdDate'])!.value,
      referalCode: this.editForm.get(['referalCode'])!.value,
      formDocumentContentType: this.editForm.get(['formDocumentContentType'])!.value,
      formDocument: this.editForm.get(['formDocument'])!.value,
      user: this.editForm.get(['user'])!.value,
    };
  }
}
