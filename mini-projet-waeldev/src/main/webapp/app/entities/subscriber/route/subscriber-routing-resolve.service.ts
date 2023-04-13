import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ISubscriber } from '../subscriber.model';
import { SubscriberService } from '../service/subscriber.service';

@Injectable({ providedIn: 'root' })
export class SubscriberRoutingResolveService implements Resolve<ISubscriber | null> {
  constructor(protected service: SubscriberService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ISubscriber | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((subscriber: HttpResponse<ISubscriber>) => {
          if (subscriber.body) {
            return of(subscriber.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(null);
  }
}
