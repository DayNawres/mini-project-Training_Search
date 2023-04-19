import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { FormationService } from 'app/entities/formation/service/formation.service';

import { FormationComponent } from 'app/entities/formation/list/formation.component';

describe('Formation Management Component', () => {
  let comp: FormationComponent;
  let fixture: ComponentFixture<FormationComponent>;
  let service: FormationService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [RouterTestingModule.withRoutes([{ path: 'formation', component: FormationComponent }]), HttpClientTestingModule],
      declarations: [FormationComponent],
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
      .overrideTemplate(FormationComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(FormationComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(FormationService);

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
    expect(comp.formations?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });

  describe('trackId', () => {
    it('Should forward to formationService', () => {
      const entity = { id: 123 };
      jest.spyOn(service, 'getFormationIdentifier');
      const id = comp.trackId(0, entity);
      expect(service.getFormationIdentifier).toHaveBeenCalledWith(entity);
      expect(id).toBe(entity.id);
    });
  });
});
