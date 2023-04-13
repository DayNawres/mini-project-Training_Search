import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { InscriptionService } from '../service/inscription.service';

import { InscriptionComponent } from './inscription.component';

describe('Inscription Management Component', () => {
  let comp: InscriptionComponent;
  let fixture: ComponentFixture<InscriptionComponent>;
  let service: InscriptionService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [RouterTestingModule.withRoutes([{ path: 'inscription', component: InscriptionComponent }]), HttpClientTestingModule],
      declarations: [InscriptionComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: {
            data: of({
              defaultSort: 'id,asc',
            }),
            queryParamMap: of(
              jest.requireActual('@angular/router').convertToParamMap({
                page: '1',
                size: '1',
                sort: 'id,desc',
              })
            ),
            snapshot: { queryParams: {} },
          },
        },
      ],
    })
      .overrideTemplate(InscriptionComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(InscriptionComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(InscriptionService);

    const headers = new HttpHeaders();
    jest.spyOn(service, 'query').mockReturnValue(
      of(
        new HttpResponse({
          body: [{ id: 123 }],
          headers,
        })
      )
    );
  });

  it('Should call load all on init', () => {
    // WHEN
    comp.ngOnInit();

    // THEN
    expect(service.query).toHaveBeenCalled();
    expect(comp.inscriptions?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });

  describe('trackId', () => {
    it('Should forward to inscriptionService', () => {
      const entity = { id: 123 };
      jest.spyOn(service, 'getInscriptionIdentifier');
      const id = comp.trackId(0, entity);
      expect(service.getInscriptionIdentifier).toHaveBeenCalledWith(entity);
      expect(id).toBe(entity.id);
    });
  });
});
