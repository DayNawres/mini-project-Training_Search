import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { FormationFormService, FormationFormGroup } from './formation-form.service';
import { IFormation } from '../formation.model';
import { FormationService } from '../service/formation.service';
import { ISubscriber } from 'app/entities/subscriber/subscriber.model';
import { SubscriberService } from 'app/entities/subscriber/service/subscriber.service';
import { Type } from 'app/entities/enumerations/type.model';

@Component({
  selector: 'jhi-formation-update',
  templateUrl: './formation-update.component.html',
})
export class FormationUpdateComponent implements OnInit {
  isSaving = false;
  formation: IFormation | null = null;
  typeValues = Object.keys(Type);

  subscribersSharedCollection: ISubscriber[] = [];

  editForm: FormationFormGroup = this.formationFormService.createFormationFormGroup();

  constructor(
    protected formationService: FormationService,
    protected formationFormService: FormationFormService,
    protected subscriberService: SubscriberService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareSubscriber = (o1: ISubscriber | null, o2: ISubscriber | null): boolean => this.subscriberService.compareSubscriber(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ formation }) => {
      this.formation = formation;
      if (formation) {
        this.updateForm(formation);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const formation = this.formationFormService.getFormation(this.editForm);
    if (formation.id !== null) {
      this.subscribeToSaveResponse(this.formationService.update(formation));
    } else {
      this.subscribeToSaveResponse(this.formationService.create(formation));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFormation>>): void {
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

  protected updateForm(formation: IFormation): void {
    this.formation = formation;
    this.formationFormService.resetForm(this.editForm, formation);

    this.subscribersSharedCollection = this.subscriberService.addSubscriberToCollectionIfMissing<ISubscriber>(
      this.subscribersSharedCollection,
      ...(formation.subscribers ?? [])
    );
  }

  protected loadRelationshipsOptions(): void {
    this.subscriberService
      .query()
      .pipe(map((res: HttpResponse<ISubscriber[]>) => res.body ?? []))
      .pipe(
        map((subscribers: ISubscriber[]) =>
          this.subscriberService.addSubscriberToCollectionIfMissing<ISubscriber>(subscribers, ...(this.formation?.subscribers ?? []))
        )
      )
      .subscribe((subscribers: ISubscriber[]) => (this.subscribersSharedCollection = subscribers));
  }
}
