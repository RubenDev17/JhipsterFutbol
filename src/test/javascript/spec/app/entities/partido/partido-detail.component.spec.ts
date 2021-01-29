import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { FutbolTestModule } from '../../../test.module';
import { PartidoDetailComponent } from 'app/entities/partido/partido-detail.component';
import { Partido } from 'app/shared/model/partido.model';

describe('Component Tests', () => {
  describe('Partido Management Detail Component', () => {
    let comp: PartidoDetailComponent;
    let fixture: ComponentFixture<PartidoDetailComponent>;
    const route = ({ data: of({ partido: new Partido(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [FutbolTestModule],
        declarations: [PartidoDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(PartidoDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(PartidoDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load partido on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.partido).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
