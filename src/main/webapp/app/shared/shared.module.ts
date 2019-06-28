import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { ConferenceSharedCommonModule, JhiLoginModalComponent, HasAnyAuthorityDirective } from './';

@NgModule({
  imports: [ConferenceSharedCommonModule],
  declarations: [JhiLoginModalComponent, HasAnyAuthorityDirective],
  entryComponents: [JhiLoginModalComponent],
  exports: [ConferenceSharedCommonModule, JhiLoginModalComponent, HasAnyAuthorityDirective],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ConferenceSharedModule {
  static forRoot() {
    return {
      ngModule: ConferenceSharedModule
    };
  }
}
