import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { FutbolTestModule } from '../../../test.module';
import { PresidenteDetailComponent } from 'app/entities/presidente/presidente-detail.component';
import { Presidente } from 'app/shared/model/presidente.model';

describe('Component Tests', () => {
  describe('Presidente Management Detail Component', () => {
    let comp: PresidenteDetailComponent;
    let fixture: ComponentFixture<PresidenteDetailComponent>;
    const route = ({ data: of({ presidente: new Presidente(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [FutbolTestModule],
        declarations: [PresidenteDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(PresidenteDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(PresidenteDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load presidente on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.presidente).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
