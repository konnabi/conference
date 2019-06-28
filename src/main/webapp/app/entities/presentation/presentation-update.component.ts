import { Component, OnInit } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { JhiAlertService } from 'ng-jhipster';
import { IPresentation, Presentation } from 'app/shared/model/presentation.model';
import { PresentationService } from './presentation.service';
import { IUser, UserService } from 'app/core';

@Component({
  selector: 'jhi-presentation-update',
  templateUrl: './presentation-update.component.html'
})
export class PresentationUpdateComponent implements OnInit {
  isSaving: boolean;

  users: IUser[];

  editForm = this.fb.group({
    id: [],
    presentationName: [null, [Validators.required]],
    presentationTheme: [],
    presentationRoom: [null, [Validators.required]],
    presentationDate: [null, [Validators.required]],
    owners: [null, Validators.required]
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected presentationService: PresentationService,
    protected userService: UserService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ presentation }) => {
      this.updateForm(presentation);
    });
    this.userService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IUser[]>) => mayBeOk.ok),
        map((response: HttpResponse<IUser[]>) => response.body)
      )
      .subscribe((res: IUser[]) => (this.users = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(presentation: IPresentation) {
    this.editForm.patchValue({
      id: presentation.id,
      presentationName: presentation.presentationName,
      presentationTheme: presentation.presentationTheme,
      presentationRoom: presentation.presentationRoom,
      presentationDate: presentation.presentationDate != null ? presentation.presentationDate.format(DATE_TIME_FORMAT) : null,
      owners: presentation.owners
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const presentation = this.createFromForm();
    if (presentation.id !== undefined) {
      this.subscribeToSaveResponse(this.presentationService.update(presentation));
    } else {
      this.subscribeToSaveResponse(this.presentationService.create(presentation));
    }
  }

  private createFromForm(): IPresentation {
    return {
      ...new Presentation(),
      id: this.editForm.get(['id']).value,
      presentationName: this.editForm.get(['presentationName']).value,
      presentationTheme: this.editForm.get(['presentationTheme']).value,
      presentationRoom: this.editForm.get(['presentationRoom']).value,
      presentationDate:
        this.editForm.get(['presentationDate']).value != null
          ? moment(this.editForm.get(['presentationDate']).value, DATE_TIME_FORMAT)
          : undefined,
      owners: this.editForm.get(['owners']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPresentation>>) {
    result.subscribe(() => this.onSaveSuccess(), () => this.onSaveError());
  }

  protected onSaveSuccess() {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError() {
    this.isSaving = false;
  }
  protected onError(errorMessage: string) {
    this.jhiAlertService.error(errorMessage, null, null);
  }

  trackUserById(index: number, item: IUser) {
    return item.id;
  }

  getSelected(selectedVals: Array<any>, option: any) {
    if (selectedVals) {
      for (let i = 0; i < selectedVals.length; i++) {
        if (option.id === selectedVals[i].id) {
          return selectedVals[i];
        }
      }
    }
    return option;
  }
}
