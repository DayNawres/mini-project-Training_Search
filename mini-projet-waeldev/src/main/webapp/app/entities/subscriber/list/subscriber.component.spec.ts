import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { SubscriberService } from '../service/subscriber.service';

import { SubscriberComponent } from './subscriber.component';

describe('Subscriber Management Component', () => {
  let comp: SubscriberComponent;
  let fixture: ComponentFixture<SubscriberComponent>;
  let service: SubscriberService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [RouterTestingModule.withRoutes([{ path: 'subscriber', component: SubscriberComponent }]), HttpClientTestingModule],
      declarations: [SubscriberComponent],
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
      .overrideTemplate(SubscriberComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(SubscriberComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(SubscriberService);

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
    expect(comp.subscribers?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });

  describe('trackId', () => {
    it('Should forward to subscriberService', () => {
      const entity = { id: 123 };
      jest.spyOn(service, 'getSubscriberIdentifier');
      const id = comp.trackId(0, entity);
      expect(service.getSubscriberIdentifier).toHaveBeenCalledWith(entity);
      expect(id).toBe(entity.id);
    });
  });
});
