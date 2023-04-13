import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { EmplacementService } from '../service/emplacement.service';

import { EmplacementComponent } from './emplacement.component';

describe('Emplacement Management Component', () => {
  let comp: EmplacementComponent;
  let fixture: ComponentFixture<EmplacementComponent>;
  let service: EmplacementService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [RouterTestingModule.withRoutes([{ path: 'emplacement', component: EmplacementComponent }]), HttpClientTestingModule],
      declarations: [EmplacementComponent],
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
      .overrideTemplate(EmplacementComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(EmplacementComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(EmplacementService);

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
    expect(comp.emplacements?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });

  describe('trackId', () => {
    it('Should forward to emplacementService', () => {
      const entity = { id: 123 };
      jest.spyOn(service, 'getEmplacementIdentifier');
      const id = comp.trackId(0, entity);
      expect(service.getEmplacementIdentifier).toHaveBeenCalledWith(entity);
      expect(id).toBe(entity.id);
    });
  });
});
