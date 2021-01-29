import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { FutbolTestModule } from '../../../test.module';
import { PartidoUpdateComponent } from 'app/entities/partido/partido-update.component';
import { PartidoService } from 'app/entities/partido/partido.service';
import { Partido } from 'app/shared/model/partido.model';

describe('Component Tests', () => {
  describe('Partido Management Update Component', () => {
    let comp: PartidoUpdateComponent;
    let fixture: ComponentFixture<PartidoUpdateComponent>;
    let service: PartidoService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [FutbolTestModule],
        declarations: [PartidoUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(PartidoUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(PartidoUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(PartidoService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Partido(123);
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
        const entity = new Partido();
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
