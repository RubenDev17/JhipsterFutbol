import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { FutbolTestModule } from '../../../test.module';
import { TemporadaUpdateComponent } from 'app/entities/temporada/temporada-update.component';
import { TemporadaService } from 'app/entities/temporada/temporada.service';
import { Temporada } from 'app/shared/model/temporada.model';

describe('Component Tests', () => {
  describe('Temporada Management Update Component', () => {
    let comp: TemporadaUpdateComponent;
    let fixture: ComponentFixture<TemporadaUpdateComponent>;
    let service: TemporadaService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [FutbolTestModule],
        declarations: [TemporadaUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(TemporadaUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(TemporadaUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(TemporadaService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Temporada(123);
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
        const entity = new Temporada();
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
