import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { FutbolTestModule } from '../../../test.module';
import { DatoDetailComponent } from 'app/entities/dato/dato-detail.component';
import { Dato } from 'app/shared/model/dato.model';

describe('Component Tests', () => {
  describe('Dato Management Detail Component', () => {
    let comp: DatoDetailComponent;
    let fixture: ComponentFixture<DatoDetailComponent>;
    const route = ({ data: of({ dato: new Dato(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [FutbolTestModule],
        declarations: [DatoDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(DatoDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(DatoDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load dato on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.dato).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
