import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { EquipoService } from 'app/entities/equipo/equipo.service';
import { IEquipo, Equipo } from 'app/shared/model/equipo.model';

describe('Service Tests', () => {
  describe('Equipo Service', () => {
    let injector: TestBed;
    let service: EquipoService;
    let httpMock: HttpTestingController;
    let elemDefault: IEquipo;
    let expectedResult: IEquipo | IEquipo[] | boolean | null;
    let currentDate: moment.Moment;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      injector = getTestBed();
      service = injector.get(EquipoService);
      httpMock = injector.get(HttpTestingController);
      currentDate = moment();

      elemDefault = new Equipo(0, 'AAAAAAA', 0, currentDate);
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            fechaDeFundacion: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a Equipo', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            fechaDeFundacion: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            fechaDeFundacion: currentDate,
          },
          returnedFromService
        );

        service.create(new Equipo()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Equipo', () => {
        const returnedFromService = Object.assign(
          {
            nombre: 'BBBBBB',
            titulos: 1,
            fechaDeFundacion: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            fechaDeFundacion: currentDate,
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Equipo', () => {
        const returnedFromService = Object.assign(
          {
            nombre: 'BBBBBB',
            titulos: 1,
            fechaDeFundacion: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            fechaDeFundacion: currentDate,
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a Equipo', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
