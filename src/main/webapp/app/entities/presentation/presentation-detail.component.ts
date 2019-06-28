import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPresentation } from 'app/shared/model/presentation.model';

@Component({
  selector: 'jhi-presentation-detail',
  templateUrl: './presentation-detail.component.html'
})
export class PresentationDetailComponent implements OnInit {
  presentation: IPresentation;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ presentation }) => {
      this.presentation = presentation;
    });
  }

  previousState() {
    window.history.back();
  }
}
