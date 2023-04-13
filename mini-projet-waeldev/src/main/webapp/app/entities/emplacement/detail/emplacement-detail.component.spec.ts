import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { EmplacementDetailComponent } from './emplacement-detail.component';

describe('Emplacement Management Detail Component', () => {
  let comp: EmplacementDetailComponent;
  let fixture: ComponentFixture<EmplacementDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [EmplacementDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ emplacement: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(EmplacementDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(EmplacementDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load emplacement on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.emplacement).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
