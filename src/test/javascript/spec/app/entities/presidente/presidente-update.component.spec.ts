import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { FutbolTestModule } from '../../../test.module';
import { PresidenteUpdateComponent } from 'app/entities/presidente/presidente-update.component';
import { PresidenteService } from 'app/entities/presidente/presidente.service';
import { Presidente } from 'app/shared/model/presidente.model';

describe('Component Tests', () => {
  describe('Presidente Management Update Component', () => {
    let comp: PresidenteUpdateComponent;
    let fixture: ComponentFixture<PresidenteUpdateComponent>;
    let service: PresidenteService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [FutbolTestModule],
        declarations: [PresidenteUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(PresidenteUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(PresidenteUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(PresidenteService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Presidente(123);
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
        const entity = new Presidente();
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
