import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { FutbolTestModule } from '../../../test.module';
import { JugadorUpdateComponent } from 'app/entities/jugador/jugador-update.component';
import { JugadorService } from 'app/entities/jugador/jugador.service';
import { Jugador } from 'app/shared/model/jugador.model';

describe('Component Tests', () => {
  describe('Jugador Management Update Component', () => {
    let comp: JugadorUpdateComponent;
    let fixture: ComponentFixture<JugadorUpdateComponent>;
    let service: JugadorService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [FutbolTestModule],
        declarations: [JugadorUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(JugadorUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(JugadorUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(JugadorService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Jugador(123);
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
        const entity = new Jugador();
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
