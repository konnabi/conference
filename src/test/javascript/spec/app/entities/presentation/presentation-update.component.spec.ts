/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { Observable, of } from 'rxjs';

import { ConferenceTestModule } from '../../../test.module';
import { PresentationUpdateComponent } from 'app/entities/presentation/presentation-update.component';
import { PresentationService } from 'app/entities/presentation/presentation.service';
import { Presentation } from 'app/shared/model/presentation.model';

describe('Component Tests', () => {
  describe('Presentation Management Update Component', () => {
    let comp: PresentationUpdateComponent;
    let fixture: ComponentFixture<PresentationUpdateComponent>;
    let service: PresentationService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [ConferenceTestModule],
        declarations: [PresentationUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(PresentationUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(PresentationUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(PresentationService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Presentation(123);
        spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.update).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));

      it('Should call create service on save for new entity', fakeAsync(() => {
        // GIVEN
        const entity = new Presentation();
        spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));
    });
  });
});
