import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { SubscriberFormService } from './subscriber-form.service';
import { SubscriberService } from '../service/subscriber.service';
import { ISubscriber } from '../subscriber.model';
import { IInscription } from 'app/entities/inscription/inscription.model';
import { InscriptionService } from 'app/entities/inscription/service/inscription.service';

import { SubscriberUpdateComponent } from './subscriber-update.component';

describe('Subscriber Management Update Component', () => {
  let comp: SubscriberUpdateComponent;
  let fixture: ComponentFixture<SubscriberUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let subscriberFormService: SubscriberFormService;
  let subscriberService: SubscriberService;
  let inscriptionService: InscriptionService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [SubscriberUpdateComponent],
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
      .overrideTemplate(SubscriberUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(SubscriberUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    subscriberFormService = TestBed.inject(SubscriberFormService);
    subscriberService = TestBed.inject(SubscriberService);
    inscriptionService = TestBed.inject(InscriptionService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Inscription query and add missing value', () => {
      const subscriber: ISubscriber = { id: 456 };
      const inscription: IInscription = { id: 63673 };
      subscriber.inscription = inscription;

      const inscriptionCollection: IInscription[] = [{ id: 76934 }];
      jest.spyOn(inscriptionService, 'query').mockReturnValue(of(new HttpResponse({ body: inscriptionCollection })));
      const additionalInscriptions = [inscription];
      const expectedCollection: IInscription[] = [...additionalInscriptions, ...inscriptionCollection];
      jest.spyOn(inscriptionService, 'addInscriptionToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ subscriber });
      comp.ngOnInit();

      expect(inscriptionService.query).toHaveBeenCalled();
      expect(inscriptionService.addInscriptionToCollectionIfMissing).toHaveBeenCalledWith(
        inscriptionCollection,
        ...additionalInscriptions.map(expect.objectContaining)
      );
      expect(comp.inscriptionsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const subscriber: ISubscriber = { id: 456 };
      const inscription: IInscription = { id: 7022 };
      subscriber.inscription = inscription;

      activatedRoute.data = of({ subscriber });
      comp.ngOnInit();

      expect(comp.inscriptionsSharedCollection).toContain(inscription);
      expect(comp.subscriber).toEqual(subscriber);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISubscriber>>();
      const subscriber = { id: 123 };
      jest.spyOn(subscriberFormService, 'getSubscriber').mockReturnValue(subscriber);
      jest.spyOn(subscriberService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ subscriber });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: subscriber }));
      saveSubject.complete();

      // THEN
      expect(subscriberFormService.getSubscriber).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(subscriberService.update).toHaveBeenCalledWith(expect.objectContaining(subscriber));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISubscriber>>();
      const subscriber = { id: 123 };
      jest.spyOn(subscriberFormService, 'getSubscriber').mockReturnValue({ id: null });
      jest.spyOn(subscriberService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ subscriber: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: subscriber }));
      saveSubject.complete();

      // THEN
      expect(subscriberFormService.getSubscriber).toHaveBeenCalled();
      expect(subscriberService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISubscriber>>();
      const subscriber = { id: 123 };
      jest.spyOn(subscriberService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ subscriber });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(subscriberService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareInscription', () => {
      it('Should forward to inscriptionService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(inscriptionService, 'compareInscription');
        comp.compareInscription(entity, entity2);
        expect(inscriptionService.compareInscription).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
