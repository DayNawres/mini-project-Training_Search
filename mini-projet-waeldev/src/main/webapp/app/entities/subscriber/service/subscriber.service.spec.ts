import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ISubscriber } from '../subscriber.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../subscriber.test-samples';

import { SubscriberService } from './subscriber.service';

const requireRestSample: ISubscriber = {
  ...sampleWithRequiredData,
};

describe('Subscriber Service', () => {
  let service: SubscriberService;
  let httpMock: HttpTestingController;
  let expectedResult: ISubscriber | ISubscriber[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(SubscriberService);
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

    it('should create a Subscriber', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const subscriber = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(subscriber).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Subscriber', () => {
      const subscriber = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(subscriber).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Subscriber', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Subscriber', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Subscriber', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addSubscriberToCollectionIfMissing', () => {
      it('should add a Subscriber to an empty array', () => {
        const subscriber: ISubscriber = sampleWithRequiredData;
        expectedResult = service.addSubscriberToCollectionIfMissing([], subscriber);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(subscriber);
      });

      it('should not add a Subscriber to an array that contains it', () => {
        const subscriber: ISubscriber = sampleWithRequiredData;
        const subscriberCollection: ISubscriber[] = [
          {
            ...subscriber,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addSubscriberToCollectionIfMissing(subscriberCollection, subscriber);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Subscriber to an array that doesn't contain it", () => {
        const subscriber: ISubscriber = sampleWithRequiredData;
        const subscriberCollection: ISubscriber[] = [sampleWithPartialData];
        expectedResult = service.addSubscriberToCollectionIfMissing(subscriberCollection, subscriber);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(subscriber);
      });

      it('should add only unique Subscriber to an array', () => {
        const subscriberArray: ISubscriber[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const subscriberCollection: ISubscriber[] = [sampleWithRequiredData];
        expectedResult = service.addSubscriberToCollectionIfMissing(subscriberCollection, ...subscriberArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const subscriber: ISubscriber = sampleWithRequiredData;
        const subscriber2: ISubscriber = sampleWithPartialData;
        expectedResult = service.addSubscriberToCollectionIfMissing([], subscriber, subscriber2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(subscriber);
        expect(expectedResult).toContain(subscriber2);
      });

      it('should accept null and undefined values', () => {
        const subscriber: ISubscriber = sampleWithRequiredData;
        expectedResult = service.addSubscriberToCollectionIfMissing([], null, subscriber, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(subscriber);
      });

      it('should return initial array if no Subscriber is added', () => {
        const subscriberCollection: ISubscriber[] = [sampleWithRequiredData];
        expectedResult = service.addSubscriberToCollectionIfMissing(subscriberCollection, undefined, null);
        expect(expectedResult).toEqual(subscriberCollection);
      });
    });

    describe('compareSubscriber', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareSubscriber(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareSubscriber(entity1, entity2);
        const compareResult2 = service.compareSubscriber(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareSubscriber(entity1, entity2);
        const compareResult2 = service.compareSubscriber(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareSubscriber(entity1, entity2);
        const compareResult2 = service.compareSubscriber(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
