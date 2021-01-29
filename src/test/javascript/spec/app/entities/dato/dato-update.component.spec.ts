import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { FutbolTestModule } from '../../../test.module';
import { DatoUpdateComponent } from 'app/entities/dato/dato-update.component';
import { DatoService } from 'app/entities/dato/dato.service';
import { Dato } from 'app/shared/model/dato.model';

describe('Component Tests', () => {
  describe('Dato Management Update Component', () => {
    let comp: DatoUpdateComponent;
    let fixture: ComponentFixture<DatoUpdateComponent>;
    let service: DatoService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [FutbolTestModule],
        declarations: [DatoUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(DatoUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(DatoUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(DatoService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Dato(123);
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
        const entity = new Dato();
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
