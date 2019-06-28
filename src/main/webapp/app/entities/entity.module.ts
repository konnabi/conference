import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { PresentationComponent } from 'app/entities/presentation';
import { ConferencePresentationModule } from 'app/entities/presentation/presentation.module';

@NgModule({
  imports: [
    ConferencePresentationModule,
    RouterModule.forChild([
      {
        path: 'presentation',
        loadChildren: './presentation/presentation.module#ConferencePresentationModule'
      }
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ])
  ],
  exports: [ConferencePresentationModule],
  declarations: [],
  entryComponents: [],
  providers: [],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ConferenceEntityModule {}
