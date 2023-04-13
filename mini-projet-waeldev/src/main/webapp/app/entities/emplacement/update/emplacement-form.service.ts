import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IEmplacement, NewEmplacement } from '../emplacement.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IEmplacement for edit and NewEmplacementFormGroupInput for create.
 */
type EmplacementFormGroupInput = IEmplacement | PartialWithRequiredKeyOf<NewEmplacement>;

type EmplacementFormDefaults = Pick<NewEmplacement, 'id'>;

type EmplacementFormGroupContent = {
  id: FormControl<IEmplacement['id'] | NewEmplacement['id']>;
  identifiant: FormControl<IEmplacement['identifiant']>;
  type: FormControl<IEmplacement['type']>;
  centreDeFormation: FormControl<IEmplacement['centreDeFormation']>;
  webSiteLink: FormControl<IEmplacement['webSiteLink']>;
  adresse: FormControl<IEmplacement['adresse']>;
  formation: FormControl<IEmplacement['formation']>;
};

export type EmplacementFormGroup = FormGroup<EmplacementFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class EmplacementFormService {
  createEmplacementFormGroup(emplacement: EmplacementFormGroupInput = { id: null }): EmplacementFormGroup {
    const emplacementRawValue = {
      ...this.getFormDefaults(),
      ...emplacement,
    };
    return new FormGroup<EmplacementFormGroupContent>({
      id: new FormControl(
        { value: emplacementRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      identifiant: new FormControl(emplacementRawValue.identifiant, {
        validators: [Validators.required],
      }),
      type: new FormControl(emplacementRawValue.type, {
        validators: [Validators.required],
      }),
      centreDeFormation: new FormControl(emplacementRawValue.centreDeFormation),
      webSiteLink: new FormControl(emplacementRawValue.webSiteLink),
      adresse: new FormControl(emplacementRawValue.adresse),
      formation: new FormControl(emplacementRawValue.formation),
    });
  }

  getEmplacement(form: EmplacementFormGroup): IEmplacement | NewEmplacement {
    return form.getRawValue() as IEmplacement | NewEmplacement;
  }

  resetForm(form: EmplacementFormGroup, emplacement: EmplacementFormGroupInput): void {
    const emplacementRawValue = { ...this.getFormDefaults(), ...emplacement };
    form.reset(
      {
        ...emplacementRawValue,
        id: { value: emplacementRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): EmplacementFormDefaults {
    return {
      id: null,
    };
  }
}
