import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { DatoService } from 'app/entities/dato/dato.service';
import { IDato, Dato } from 'app/shared/model/dato.model';

describe('Service Tests', () => {
  describe('Dato Service', () => {
    let injector: TestBed;
    let service: DatoService;
    let httpMock: HttpTestingController;
    let elemDefault: IDato;
    let expectedResult: IDato | IDato[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      injector = getTestBed();
      service = injector.get(DatoService);
      httpMock = injector.get(HttpTestingController);

      elemDefault = new Dato(0, 0, 'AAAAAAA', 'AAAAAAA', 0, 0, 0);
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign({}, elemDefault);

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a Dato', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new Dato()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Dato', () => {
        const returnedFromService = Object.assign(
          {
            jornada: 1,
            rival: 'BBBBBB',
            resultado: 'BBBBBB',
            numeroDeGoles: 1,
            corner: 1,
            faltas: 1,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Dato', () => {
        const returnedFromService = Object.assign(
          {
            jornada: 1,
            rival: 'BBBBBB',
            resultado: 'BBBBBB',
            numeroDeGoles: 1,
            corner: 1,
            faltas: 1,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a Dato', () => {
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
