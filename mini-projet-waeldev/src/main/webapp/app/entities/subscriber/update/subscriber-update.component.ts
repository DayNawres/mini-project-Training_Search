import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { SubscriberFormService, SubscriberFormGroup } from './subscriber-form.service';
import { ISubscriber } from '../subscriber.model';
import { SubscriberService } from '../service/subscriber.service';
import { IInscription } from 'app/entities/inscription/inscription.model';
import { InscriptionService } from 'app/entities/inscription/service/inscription.service';

@Component({
  selector: 'jhi-subscriber-update',
  templateUrl: './subscriber-update.component.html',
})
export class SubscriberUpdateComponent implements OnInit {
  isSaving = false;
  subscriber: ISubscriber | null = null;

  inscriptionsSharedCollection: IInscription[] = [];

  editForm: SubscriberFormGroup = this.subscriberFormService.createSubscriberFormGroup();

  constructor(
    protected subscriberService: SubscriberService,
    protected subscriberFormService: SubscriberFormService,
    protected inscriptionService: InscriptionService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareInscription = (o1: IInscription | null, o2: IInscription | null): boolean => this.inscriptionService.compareInscription(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ subscriber }) => {
      this.subscriber = subscriber;
      if (subscriber) {
        this.updateForm(subscriber);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const subscriber = this.subscriberFormService.getSubscriber(this.editForm);
    if (subscriber.id !== null) {
      this.subscribeToSaveResponse(this.subscriberService.update(subscriber));
    } else {
      this.subscribeToSaveResponse(this.subscriberService.create(subscriber));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISubscriber>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(subscriber: ISubscriber): void {
    this.subscriber = subscriber;
    this.subscriberFormService.resetForm(this.editForm, subscriber);

    this.inscriptionsSharedCollection = this.inscriptionService.addInscriptionToCollectionIfMissing<IInscription>(
      this.inscriptionsSharedCollection,
      subscriber.inscription
    );
  }

  protected loadRelationshipsOptions(): void {
    this.inscriptionService
      .query()
      .pipe(map((res: HttpResponse<IInscription[]>) => res.body ?? []))
      .pipe(
        map((inscriptions: IInscription[]) =>
          this.inscriptionService.addInscriptionToCollectionIfMissing<IInscription>(inscriptions, this.subscriber?.inscription)
        )
      )
      .subscribe((inscriptions: IInscription[]) => (this.inscriptionsSharedCollection = inscriptions));
  }
}
