import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../emplacement.test-samples';

import { EmplacementFormService } from './emplacement-form.service';

describe('Emplacement Form Service', () => {
  let service: EmplacementFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(EmplacementFormService);
  });

  describe('Service methods', () => {
    describe('createEmplacementFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createEmplacementFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            identifiant: expect.any(Object),
            type: expect.any(Object),
            centreDeFormation: expect.any(Object),
            webSiteLink: expect.any(Object),
            adresse: expect.any(Object),
            formation: expect.any(Object),
          })
        );
      });

      it('passing IEmplacement should create a new form with FormGroup', () => {
        const formGroup = service.createEmplacementFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            identifiant: expect.any(Object),
            type: expect.any(Object),
            centreDeFormation: expect.any(Object),
            webSiteLink: expect.any(Object),
            adresse: expect.any(Object),
            formation: expect.any(Object),
          })
        );
      });
    });

    describe('getEmplacement', () => {
      it('should return NewEmplacement for default Emplacement initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createEmplacementFormGroup(sampleWithNewData);

        const emplacement = service.getEmplacement(formGroup) as any;

        expect(emplacement).toMatchObject(sampleWithNewData);
      });

      it('should return NewEmplacement for empty Emplacement initial value', () => {
        const formGroup = service.createEmplacementFormGroup();

        const emplacement = service.getEmplacement(formGroup) as any;

        expect(emplacement).toMatchObject({});
      });

      it('should return IEmplacement', () => {
        const formGroup = service.createEmplacementFormGroup(sampleWithRequiredData);

        const emplacement = service.getEmplacement(formGroup) as any;

        expect(emplacement).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IEmplacement should not enable id FormControl', () => {
        const formGroup = service.createEmplacementFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewEmplacement should disable id FormControl', () => {
        const formGroup = service.createEmplacementFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
