import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { FutbolTestModule } from '../../../test.module';
import { LigaDetailComponent } from 'app/entities/liga/liga-detail.component';
import { Liga } from 'app/shared/model/liga.model';

describe('Component Tests', () => {
  describe('Liga Management Detail Component', () => {
    let comp: LigaDetailComponent;
    let fixture: ComponentFixture<LigaDetailComponent>;
    const route = ({ data: of({ liga: new Liga(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [FutbolTestModule],
        declarations: [LigaDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(LigaDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(LigaDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load liga on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.liga).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
