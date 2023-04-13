import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IEmplacement } from '../emplacement.model';
import { EmplacementService } from '../service/emplacement.service';

@Injectable({ providedIn: 'root' })
export class EmplacementRoutingResolveService implements Resolve<IEmplacement | null> {
  constructor(protected service: EmplacementService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IEmplacement | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((emplacement: HttpResponse<IEmplacement>) => {
          if (emplacement.body) {
            return of(emplacement.body);
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
