import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../subscriber.test-samples';

import { SubscriberFormService } from './subscriber-form.service';

describe('Subscriber Form Service', () => {
  let service: SubscriberFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SubscriberFormService);
  });

  describe('Service methods', () => {
    describe('createSubscriberFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createSubscriberFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            cIN: expect.any(Object),
            nom: expect.any(Object),
            prenom: expect.any(Object),
            age: expect.any(Object),
            statut: expect.any(Object),
            inscription: expect.any(Object),
            formations: expect.any(Object),
          })
        );
      });

      it('passing ISubscriber should create a new form with FormGroup', () => {
        const formGroup = service.createSubscriberFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            cIN: expect.any(Object),
            nom: expect.any(Object),
            prenom: expect.any(Object),
            age: expect.any(Object),
            statut: expect.any(Object),
            inscription: expect.any(Object),
            formations: expect.any(Object),
          })
        );
      });
    });

    describe('getSubscriber', () => {
      it('should return NewSubscriber for default Subscriber initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createSubscriberFormGroup(sampleWithNewData);

        const subscriber = service.getSubscriber(formGroup) as any;

        expect(subscriber).toMatchObject(sampleWithNewData);
      });

      it('should return NewSubscriber for empty Subscriber initial value', () => {
        const formGroup = service.createSubscriberFormGroup();

        const subscriber = service.getSubscriber(formGroup) as any;

        expect(subscriber).toMatchObject({});
      });

      it('should return ISubscriber', () => {
        const formGroup = service.createSubscriberFormGroup(sampleWithRequiredData);

        const subscriber = service.getSubscriber(formGroup) as any;

        expect(subscriber).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ISubscriber should not enable id FormControl', () => {
        const formGroup = service.createSubscriberFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewSubscriber should disable id FormControl', () => {
        const formGroup = service.createSubscriberFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
