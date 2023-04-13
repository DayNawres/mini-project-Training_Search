import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IEmplacement, NewEmplacement } from '../emplacement.model';

export type PartialUpdateEmplacement = Partial<IEmplacement> & Pick<IEmplacement, 'id'>;

export type EntityResponseType = HttpResponse<IEmplacement>;
export type EntityArrayResponseType = HttpResponse<IEmplacement[]>;

@Injectable({ providedIn: 'root' })
export class EmplacementService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/emplacements');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(emplacement: NewEmplacement): Observable<EntityResponseType> {
    return this.http.post<IEmplacement>(this.resourceUrl, emplacement, { observe: 'response' });
  }

  update(emplacement: IEmplacement): Observable<EntityResponseType> {
    return this.http.put<IEmplacement>(`${this.resourceUrl}/${this.getEmplacementIdentifier(emplacement)}`, emplacement, {
      observe: 'response',
    });
  }

  partialUpdate(emplacement: PartialUpdateEmplacement): Observable<EntityResponseType> {
    return this.http.patch<IEmplacement>(`${this.resourceUrl}/${this.getEmplacementIdentifier(emplacement)}`, emplacement, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IEmplacement>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IEmplacement[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getEmplacementIdentifier(emplacement: Pick<IEmplacement, 'id'>): number {
    return emplacement.id;
  }

  compareEmplacement(o1: Pick<IEmplacement, 'id'> | null, o2: Pick<IEmplacement, 'id'> | null): boolean {
    return o1 && o2 ? this.getEmplacementIdentifier(o1) === this.getEmplacementIdentifier(o2) : o1 === o2;
  }

  addEmplacementToCollectionIfMissing<Type extends Pick<IEmplacement, 'id'>>(
    emplacementCollection: Type[],
    ...emplacementsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const emplacements: Type[] = emplacementsToCheck.filter(isPresent);
    if (emplacements.length > 0) {
      const emplacementCollectionIdentifiers = emplacementCollection.map(
        emplacementItem => this.getEmplacementIdentifier(emplacementItem)!
      );
      const emplacementsToAdd = emplacements.filter(emplacementItem => {
        const emplacementIdentifier = this.getEmplacementIdentifier(emplacementItem);
        if (emplacementCollectionIdentifiers.includes(emplacementIdentifier)) {
          return false;
        }
        emplacementCollectionIdentifiers.push(emplacementIdentifier);
        return true;
      });
      return [...emplacementsToAdd, ...emplacementCollection];
    }
    return emplacementCollection;
  }
}
