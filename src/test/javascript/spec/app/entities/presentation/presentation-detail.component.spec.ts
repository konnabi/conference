/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ConferenceTestModule } from '../../../test.module';
import { PresentationDetailComponent } from 'app/entities/presentation/presentation-detail.component';
import { Presentation } from 'app/shared/model/presentation.model';

describe('Component Tests', () => {
  describe('Presentation Management Detail Component', () => {
    let comp: PresentationDetailComponent;
    let fixture: ComponentFixture<PresentationDetailComponent>;
    const route = ({ data: of({ presentation: new Presentation(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [ConferenceTestModule],
        declarations: [PresentationDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(PresentationDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(PresentationDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.presentation).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
