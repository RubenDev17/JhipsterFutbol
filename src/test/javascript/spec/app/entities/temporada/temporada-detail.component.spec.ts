import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { FutbolTestModule } from '../../../test.module';
import { TemporadaDetailComponent } from 'app/entities/temporada/temporada-detail.component';
import { Temporada } from 'app/shared/model/temporada.model';

describe('Component Tests', () => {
  describe('Temporada Management Detail Component', () => {
    let comp: TemporadaDetailComponent;
    let fixture: ComponentFixture<TemporadaDetailComponent>;
    const route = ({ data: of({ temporada: new Temporada(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [FutbolTestModule],
        declarations: [TemporadaDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(TemporadaDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(TemporadaDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load temporada on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.temporada).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
