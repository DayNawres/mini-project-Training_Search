import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { SubscriberDetailComponent } from './subscriber-detail.component';

describe('Subscriber Management Detail Component', () => {
  let comp: SubscriberDetailComponent;
  let fixture: ComponentFixture<SubscriberDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [SubscriberDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ subscriber: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(SubscriberDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(SubscriberDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load subscriber on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.subscriber).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
