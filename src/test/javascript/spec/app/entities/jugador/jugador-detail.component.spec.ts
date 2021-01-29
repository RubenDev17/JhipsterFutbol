import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { FutbolTestModule } from '../../../test.module';
import { JugadorDetailComponent } from 'app/entities/jugador/jugador-detail.component';
import { Jugador } from 'app/shared/model/jugador.model';

describe('Component Tests', () => {
  describe('Jugador Management Detail Component', () => {
    let comp: JugadorDetailComponent;
    let fixture: ComponentFixture<JugadorDetailComponent>;
    const route = ({ data: of({ jugador: new Jugador(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [FutbolTestModule],
        declarations: [JugadorDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(JugadorDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(JugadorDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load jugador on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.jugador).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
