import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IFormation, NewFormation } from '../formation.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IFormation for edit and NewFormationFormGroupInput for create.
 */
type FormationFormGroupInput = IFormation | PartialWithRequiredKeyOf<NewFormation>;

type FormationFormDefaults = Pick<NewFormation, 'id' | 'subscribers'>;

type FormationFormGroupContent = {
  id: FormControl<IFormation['id'] | NewFormation['id']>;
  identifiant: FormControl<IFormation['identifiant']>;
  description: FormControl<IFormation['description']>;
  domaine: FormControl<IFormation['domaine']>;
  type: FormControl<IFormation['type']>;
  dateDebut: FormControl<IFormation['dateDebut']>;
  dateFin: FormControl<IFormation['dateFin']>;
  lesHeuresDeLaFormation: FormControl<IFormation['lesHeuresDeLaFormation']>;
  lePrix: FormControl<IFormation['lePrix']>;
  nomInstructeur: FormControl<IFormation['nomInstructeur']>;
  subscribers: FormControl<IFormation['subscribers']>;
};

export type FormationFormGroup = FormGroup<FormationFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class FormationFormService {
  createFormationFormGroup(formation: FormationFormGroupInput = { id: null }): FormationFormGroup {
    const formationRawValue = {
      ...this.getFormDefaults(),
      ...formation,
    };
    return new FormGroup<FormationFormGroupContent>({
      id: new FormControl(
        { value: formationRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      identifiant: new FormControl(formationRawValue.identifiant, {
        validators: [Validators.required],
      }),
      description: new FormControl(formationRawValue.description, {
        validators: [Validators.required],
      }),
      domaine: new FormControl(formationRawValue.domaine),
      type: new FormControl(formationRawValue.type),
      dateDebut: new FormControl(formationRawValue.dateDebut, {
        validators: [Validators.required],
      }),
      dateFin: new FormControl(formationRawValue.dateFin, {
        validators: [Validators.required],
      }),
      lesHeuresDeLaFormation: new FormControl(formationRawValue.lesHeuresDeLaFormation, {
        validators: [Validators.required],
      }),
      lePrix: new FormControl(formationRawValue.lePrix),
      nomInstructeur: new FormControl(formationRawValue.nomInstructeur),
      subscribers: new FormControl(formationRawValue.subscribers ?? []),
    });
  }

  getFormation(form: FormationFormGroup): IFormation | NewFormation {
    return form.getRawValue() as IFormation | NewFormation;
  }

  resetForm(form: FormationFormGroup, formation: FormationFormGroupInput): void {
    const formationRawValue = { ...this.getFormDefaults(), ...formation };
    form.reset(
      {
        ...formationRawValue,
        id: { value: formationRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): FormationFormDefaults {
    return {
      id: null,
      subscribers: [],
    };
  }
}
