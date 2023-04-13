import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { EmplacementFormService } from './emplacement-form.service';
import { EmplacementService } from '../service/emplacement.service';
import { IEmplacement } from '../emplacement.model';
import { IFormation } from 'app/entities/formation/formation.model';
import { FormationService } from 'app/entities/formation/service/formation.service';

import { EmplacementUpdateComponent } from './emplacement-update.component';

describe('Emplacement Management Update Component', () => {
  let comp: EmplacementUpdateComponent;
  let fixture: ComponentFixture<EmplacementUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let emplacementFormService: EmplacementFormService;
  let emplacementService: EmplacementService;
  let formationService: FormationService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [EmplacementUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(EmplacementUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(EmplacementUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    emplacementFormService = TestBed.inject(EmplacementFormService);
    emplacementService = TestBed.inject(EmplacementService);
    formationService = TestBed.inject(FormationService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Formation query and add missing value', () => {
      const emplacement: IEmplacement = { id: 456 };
      const formation: IFormation = { id: 1520 };
      emplacement.formation = formation;

      const formationCollection: IFormation[] = [{ id: 19389 }];
      jest.spyOn(formationService, 'query').mockReturnValue(of(new HttpResponse({ body: formationCollection })));
      const additionalFormations = [formation];
      const expectedCollection: IFormation[] = [...additionalFormations, ...formationCollection];
      jest.spyOn(formationService, 'addFormationToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ emplacement });
      comp.ngOnInit();

      expect(formationService.query).toHaveBeenCalled();
      expect(formationService.addFormationToCollectionIfMissing).toHaveBeenCalledWith(
        formationCollection,
        ...additionalFormations.map(expect.objectContaining)
      );
      expect(comp.formationsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const emplacement: IEmplacement = { id: 456 };
      const formation: IFormation = { id: 83645 };
      emplacement.formation = formation;

      activatedRoute.data = of({ emplacement });
      comp.ngOnInit();

      expect(comp.formationsSharedCollection).toContain(formation);
      expect(comp.emplacement).toEqual(emplacement);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEmplacement>>();
      const emplacement = { id: 123 };
      jest.spyOn(emplacementFormService, 'getEmplacement').mockReturnValue(emplacement);
      jest.spyOn(emplacementService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ emplacement });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: emplacement }));
      saveSubject.complete();

      // THEN
      expect(emplacementFormService.getEmplacement).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(emplacementService.update).toHaveBeenCalledWith(expect.objectContaining(emplacement));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEmplacement>>();
      const emplacement = { id: 123 };
      jest.spyOn(emplacementFormService, 'getEmplacement').mockReturnValue({ id: null });
      jest.spyOn(emplacementService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ emplacement: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: emplacement }));
      saveSubject.complete();

      // THEN
      expect(emplacementFormService.getEmplacement).toHaveBeenCalled();
      expect(emplacementService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEmplacement>>();
      const emplacement = { id: 123 };
      jest.spyOn(emplacementService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ emplacement });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(emplacementService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareFormation', () => {
      it('Should forward to formationService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(formationService, 'compareFormation');
        comp.compareFormation(entity, entity2);
        expect(formationService.compareFormation).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
