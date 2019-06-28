import { NgModule } from '@angular/core';

import { ConferenceSharedLibsModule, JhiAlertComponent, JhiAlertErrorComponent } from './';

@NgModule({
  imports: [ConferenceSharedLibsModule],
  declarations: [JhiAlertComponent, JhiAlertErrorComponent],
  exports: [ConferenceSharedLibsModule, JhiAlertComponent, JhiAlertErrorComponent]
})
export class ConferenceSharedCommonModule {}
