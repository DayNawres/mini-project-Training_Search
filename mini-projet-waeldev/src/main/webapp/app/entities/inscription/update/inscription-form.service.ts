import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IInscription, NewInscription } from '../inscription.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IInscription for edit and NewInscriptionFormGroupInput for create.
 */
type InscriptionFormGroupInput = IInscription | PartialWithRequiredKeyOf<NewInscription>;

type InscriptionFormDefaults = Pick<NewInscription, 'id'>;

type InscriptionFormGroupContent = {
  id: FormControl<IInscription['id'] | NewInscription['id']>;
  objet: FormControl<IInscription['objet']>;
  dateValiditeDebut: FormControl<IInscription['dateValiditeDebut']>;
  dateValiditeFin: FormControl<IInscription['dateValiditeFin']>;
  formation: FormControl<IInscription['formation']>;
};

export type InscriptionFormGroup = FormGroup<InscriptionFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class InscriptionFormService {
  createInscriptionFormGroup(inscription: InscriptionFormGroupInput = { id: null }): InscriptionFormGroup {
    const inscriptionRawValue = {
      ...this.getFormDefaults(),
      ...inscription,
    };
    return new FormGroup<InscriptionFormGroupContent>({
      id: new FormControl(
        { value: inscriptionRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      objet: new FormControl(inscriptionRawValue.objet, {
        validators: [Validators.required],
      }),
      dateValiditeDebut: new FormControl(inscriptionRawValue.dateValiditeDebut, {
        validators: [Validators.required],
      }),
      dateValiditeFin: new FormControl(inscriptionRawValue.dateValiditeFin, {
        validators: [Validators.required],
      }),
      formation: new FormControl(inscriptionRawValue.formation, {
        validators: [Validators.required],
      }),
    });
  }

  getInscription(form: InscriptionFormGroup): IInscription | NewInscription {
    return form.getRawValue() as IInscription | NewInscription;
  }

  resetForm(form: InscriptionFormGroup, inscription: InscriptionFormGroupInput): void {
    const inscriptionRawValue = { ...this.getFormDefaults(), ...inscription };
    form.reset(
      {
        ...inscriptionRawValue,
        id: { value: inscriptionRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): InscriptionFormDefaults {
    return {
      id: null,
    };
  }
}
