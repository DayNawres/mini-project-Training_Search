import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IEmplacement } from '../emplacement.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../emplacement.test-samples';

import { EmplacementService } from './emplacement.service';

const requireRestSample: IEmplacement = {
  ...sampleWithRequiredData,
};

describe('Emplacement Service', () => {
  let service: EmplacementService;
  let httpMock: HttpTestingController;
  let expectedResult: IEmplacement | IEmplacement[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(EmplacementService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a Emplacement', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const emplacement = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(emplacement).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Emplacement', () => {
      const emplacement = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(emplacement).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Emplacement', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Emplacement', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Emplacement', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addEmplacementToCollectionIfMissing', () => {
      it('should add a Emplacement to an empty array', () => {
        const emplacement: IEmplacement = sampleWithRequiredData;
        expectedResult = service.addEmplacementToCollectionIfMissing([], emplacement);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(emplacement);
      });

      it('should not add a Emplacement to an array that contains it', () => {
        const emplacement: IEmplacement = sampleWithRequiredData;
        const emplacementCollection: IEmplacement[] = [
          {
            ...emplacement,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addEmplacementToCollectionIfMissing(emplacementCollection, emplacement);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Emplacement to an array that doesn't contain it", () => {
        const emplacement: IEmplacement = sampleWithRequiredData;
        const emplacementCollection: IEmplacement[] = [sampleWithPartialData];
        expectedResult = service.addEmplacementToCollectionIfMissing(emplacementCollection, emplacement);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(emplacement);
      });

      it('should add only unique Emplacement to an array', () => {
        const emplacementArray: IEmplacement[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const emplacementCollection: IEmplacement[] = [sampleWithRequiredData];
        expectedResult = service.addEmplacementToCollectionIfMissing(emplacementCollection, ...emplacementArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const emplacement: IEmplacement = sampleWithRequiredData;
        const emplacement2: IEmplacement = sampleWithPartialData;
        expectedResult = service.addEmplacementToCollectionIfMissing([], emplacement, emplacement2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(emplacement);
        expect(expectedResult).toContain(emplacement2);
      });

      it('should accept null and undefined values', () => {
        const emplacement: IEmplacement = sampleWithRequiredData;
        expectedResult = service.addEmplacementToCollectionIfMissing([], null, emplacement, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(emplacement);
      });

      it('should return initial array if no Emplacement is added', () => {
        const emplacementCollection: IEmplacement[] = [sampleWithRequiredData];
        expectedResult = service.addEmplacementToCollectionIfMissing(emplacementCollection, undefined, null);
        expect(expectedResult).toEqual(emplacementCollection);
      });
    });

    describe('compareEmplacement', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareEmplacement(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareEmplacement(entity1, entity2);
        const compareResult2 = service.compareEmplacement(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareEmplacement(entity1, entity2);
        const compareResult2 = service.compareEmplacement(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareEmplacement(entity1, entity2);
        const compareResult2 = service.compareEmplacement(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
