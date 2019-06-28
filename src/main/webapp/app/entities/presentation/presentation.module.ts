import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ConferenceSharedModule } from 'app/shared';
import {
  PresentationComponent,
  PresentationDetailComponent,
  PresentationUpdateComponent,
  PresentationDeletePopupComponent,
  PresentationDeleteDialogComponent,
  presentationRoute,
  presentationPopupRoute
} from './';

const ENTITY_STATES = [...presentationRoute, ...presentationPopupRoute];

@NgModule({
  imports: [ConferenceSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    PresentationComponent,
    PresentationDetailComponent,
    PresentationUpdateComponent,
    PresentationDeleteDialogComponent,
    PresentationDeletePopupComponent
  ],
  entryComponents: [
    PresentationComponent,
    PresentationUpdateComponent,
    PresentationDeleteDialogComponent,
    PresentationDeletePopupComponent
  ],
  exports: [PresentationComponent],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ConferencePresentationModule {}
