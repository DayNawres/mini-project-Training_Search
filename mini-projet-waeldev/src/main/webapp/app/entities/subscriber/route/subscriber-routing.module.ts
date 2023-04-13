import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { SubscriberComponent } from '../list/subscriber.component';
import { SubscriberDetailComponent } from '../detail/subscriber-detail.component';
import { SubscriberUpdateComponent } from '../update/subscriber-update.component';
import { SubscriberRoutingResolveService } from './subscriber-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const subscriberRoute: Routes = [
  {
    path: '',
    component: SubscriberComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: SubscriberDetailComponent,
    resolve: {
      subscriber: SubscriberRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: SubscriberUpdateComponent,
    resolve: {
      subscriber: SubscriberRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: SubscriberUpdateComponent,
    resolve: {
      subscriber: SubscriberRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(subscriberRoute)],
  exports: [RouterModule],
})
export class SubscriberRoutingModule {}
