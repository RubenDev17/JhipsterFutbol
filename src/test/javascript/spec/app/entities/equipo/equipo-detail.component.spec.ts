import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { FutbolTestModule } from '../../../test.module';
import { EquipoDetailComponent } from 'app/entities/equipo/equipo-detail.component';
import { Equipo } from 'app/shared/model/equipo.model';

describe('Component Tests', () => {
  describe('Equipo Management Detail Component', () => {
    let comp: EquipoDetailComponent;
    let fixture: ComponentFixture<EquipoDetailComponent>;
    const route = ({ data: of({ equipo: new Equipo(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [FutbolTestModule],
        declarations: [EquipoDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(EquipoDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(EquipoDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load equipo on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.equipo).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
