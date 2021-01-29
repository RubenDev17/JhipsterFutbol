import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { FutbolTestModule } from '../../../test.module';
import { LigaUpdateComponent } from 'app/entities/liga/liga-update.component';
import { LigaService } from 'app/entities/liga/liga.service';
import { Liga } from 'app/shared/model/liga.model';

describe('Component Tests', () => {
  describe('Liga Management Update Component', () => {
    let comp: LigaUpdateComponent;
    let fixture: ComponentFixture<LigaUpdateComponent>;
    let service: LigaService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [FutbolTestModule],
        declarations: [LigaUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(LigaUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(LigaUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(LigaService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Liga(123);
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
        const entity = new Liga();
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
