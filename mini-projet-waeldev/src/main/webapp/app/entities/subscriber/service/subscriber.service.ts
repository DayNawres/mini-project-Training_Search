import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ISubscriber, NewSubscriber } from '../subscriber.model';

export type PartialUpdateSubscriber = Partial<ISubscriber> & Pick<ISubscriber, 'id'>;

export type EntityResponseType = HttpResponse<ISubscriber>;
export type EntityArrayResponseType = HttpResponse<ISubscriber[]>;

@Injectable({ providedIn: 'root' })
export class SubscriberService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/subscribers');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(subscriber: NewSubscriber): Observable<EntityResponseType> {
    return this.http.post<ISubscriber>(this.resourceUrl, subscriber, { observe: 'response' });
  }

  update(subscriber: ISubscriber): Observable<EntityResponseType> {
    return this.http.put<ISubscriber>(`${this.resourceUrl}/${this.getSubscriberIdentifier(subscriber)}`, subscriber, {
      observe: 'response',
    });
  }

  partialUpdate(subscriber: PartialUpdateSubscriber): Observable<EntityResponseType> {
    return this.http.patch<ISubscriber>(`${this.resourceUrl}/${this.getSubscriberIdentifier(subscriber)}`, subscriber, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ISubscriber>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ISubscriber[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getSubscriberIdentifier(subscriber: Pick<ISubscriber, 'id'>): number {
    return subscriber.id;
  }

  compareSubscriber(o1: Pick<ISubscriber, 'id'> | null, o2: Pick<ISubscriber, 'id'> | null): boolean {
    return o1 && o2 ? this.getSubscriberIdentifier(o1) === this.getSubscriberIdentifier(o2) : o1 === o2;
  }

  addSubscriberToCollectionIfMissing<Type extends Pick<ISubscriber, 'id'>>(
    subscriberCollection: Type[],
    ...subscribersToCheck: (Type | null | undefined)[]
  ): Type[] {
    const subscribers: Type[] = subscribersToCheck.filter(isPresent);
    if (subscribers.length > 0) {
      const subscriberCollectionIdentifiers = subscriberCollection.map(subscriberItem => this.getSubscriberIdentifier(subscriberItem)!);
      const subscribersToAdd = subscribers.filter(subscriberItem => {
        const subscriberIdentifier = this.getSubscriberIdentifier(subscriberItem);
        if (subscriberCollectionIdentifiers.includes(subscriberIdentifier)) {
          return false;
        }
        subscriberCollectionIdentifiers.push(subscriberIdentifier);
        return true;
      });
      return [...subscribersToAdd, ...subscriberCollection];
    }
    return subscriberCollection;
  }
}
