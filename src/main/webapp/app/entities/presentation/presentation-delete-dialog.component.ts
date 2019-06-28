import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IPresentation } from 'app/shared/model/presentation.model';
import { PresentationService } from './presentation.service';

@Component({
  selector: 'jhi-presentation-delete-dialog',
  templateUrl: './presentation-delete-dialog.component.html'
})
export class PresentationDeleteDialogComponent {
  presentation: IPresentation;

  constructor(
    protected presentationService: PresentationService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.presentationService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'presentationListModification',
        content: 'Deleted an presentation'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-presentation-delete-popup',
  template: ''
})
export class PresentationDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ presentation }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(PresentationDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.presentation = presentation;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/presentation', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/presentation', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          }
        );
      }, 0);
    });
  }

  ngOnDestroy() {
    this.ngbModalRef = null;
  }
}
