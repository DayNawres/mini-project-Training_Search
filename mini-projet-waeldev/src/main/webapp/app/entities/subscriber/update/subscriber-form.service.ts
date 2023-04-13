import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ISubscriber, NewSubscriber } from '../subscriber.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ISubscriber for edit and NewSubscriberFormGroupInput for create.
 */
type SubscriberFormGroupInput = ISubscriber | PartialWithRequiredKeyOf<NewSubscriber>;

type SubscriberFormDefaults = Pick<NewSubscriber, 'id' | 'formations'>;

type SubscriberFormGroupContent = {
  id: FormControl<ISubscriber['id'] | NewSubscriber['id']>;
  cIN: FormControl<ISubscriber['cIN']>;
  nom: FormControl<ISubscriber['nom']>;
  prenom: FormControl<ISubscriber['prenom']>;
  age: FormControl<ISubscriber['age']>;
  statut: FormControl<ISubscriber['statut']>;
  inscription: FormControl<ISubscriber['inscription']>;
  formations: FormControl<ISubscriber['formations']>;
};

export type SubscriberFormGroup = FormGroup<SubscriberFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class SubscriberFormService {
  createSubscriberFormGroup(subscriber: SubscriberFormGroupInput = { id: null }): SubscriberFormGroup {
    const subscriberRawValue = {
      ...this.getFormDefaults(),
      ...subscriber,
    };
    return new FormGroup<SubscriberFormGroupContent>({
      id: new FormControl(
        { value: subscriberRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      cIN: new FormControl(subscriberRawValue.cIN, {
        validators: [Validators.required],
      }),
      nom: new FormControl(subscriberRawValue.nom, {
        validators: [Validators.required],
      }),
      prenom: new FormControl(subscriberRawValue.prenom, {
        validators: [Validators.required],
      }),
      age: new FormControl(subscriberRawValue.age),
      statut: new FormControl(subscriberRawValue.statut, {
        validators: [Validators.required],
      }),
      inscription: new FormControl(subscriberRawValue.inscription),
      formations: new FormControl(subscriberRawValue.formations ?? []),
    });
  }

  getSubscriber(form: SubscriberFormGroup): ISubscriber | NewSubscriber {
    return form.getRawValue() as ISubscriber | NewSubscriber;
  }

  resetForm(form: SubscriberFormGroup, subscriber: SubscriberFormGroupInput): void {
    const subscriberRawValue = { ...this.getFormDefaults(), ...subscriber };
    form.reset(
      {
        ...subscriberRawValue,
        id: { value: subscriberRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): SubscriberFormDefaults {
    return {
      id: null,
      formations: [],
    };
  }
}
