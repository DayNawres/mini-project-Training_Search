import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { EmplacementFormService, EmplacementFormGroup } from './emplacement-form.service';
import { IEmplacement } from '../emplacement.model';
import { EmplacementService } from '../service/emplacement.service';
import { IFormation } from 'app/entities/formation/formation.model';
import { FormationService } from 'app/entities/formation/service/formation.service';

@Component({
  selector: 'jhi-emplacement-update',
  templateUrl: './emplacement-update.component.html',
})
export class EmplacementUpdateComponent implements OnInit {
  isSaving = false;
  emplacement: IEmplacement | null = null;

  formationsSharedCollection: IFormation[] = [];

  editForm: EmplacementFormGroup = this.emplacementFormService.createEmplacementFormGroup();

  constructor(
    protected emplacementService: EmplacementService,
    protected emplacementFormService: EmplacementFormService,
    protected formationService: FormationService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareFormation = (o1: IFormation | null, o2: IFormation | null): boolean => this.formationService.compareFormation(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ emplacement }) => {
      this.emplacement = emplacement;
      if (emplacement) {
        this.updateForm(emplacement);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const emplacement = this.emplacementFormService.getEmplacement(this.editForm);
    if (emplacement.id !== null) {
      this.subscribeToSaveResponse(this.emplacementService.update(emplacement));
    } else {
      this.subscribeToSaveResponse(this.emplacementService.create(emplacement));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEmplacement>>): void {
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

  protected updateForm(emplacement: IEmplacement): void {
    this.emplacement = emplacement;
    this.emplacementFormService.resetForm(this.editForm, emplacement);

    this.formationsSharedCollection = this.formationService.addFormationToCollectionIfMissing<IFormation>(
      this.formationsSharedCollection,
      emplacement.formation
    );
  }

  protected loadRelationshipsOptions(): void {
    this.formationService
      .query()
      .pipe(map((res: HttpResponse<IFormation[]>) => res.body ?? []))
      .pipe(
        map((formations: IFormation[]) =>
          this.formationService.addFormationToCollectionIfMissing<IFormation>(formations, this.emplacement?.formation)
        )
      )
      .subscribe((formations: IFormation[]) => (this.formationsSharedCollection = formations));
  }
}
