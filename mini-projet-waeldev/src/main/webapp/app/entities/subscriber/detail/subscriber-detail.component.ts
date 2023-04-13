import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ISubscriber } from '../subscriber.model';

@Component({
  selector: 'jhi-subscriber-detail',
  templateUrl: './subscriber-detail.component.html',
})
export class SubscriberDetailComponent implements OnInit {
  subscriber: ISubscriber | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ subscriber }) => {
      this.subscriber = subscriber;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
