import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { EmplacementComponent } from '../list/emplacement.component';
import { EmplacementDetailComponent } from '../detail/emplacement-detail.component';
import { EmplacementUpdateComponent } from '../update/emplacement-update.component';
import { EmplacementRoutingResolveService } from './emplacement-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const emplacementRoute: Routes = [
  {
    path: '',
    component: EmplacementComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: EmplacementDetailComponent,
    resolve: {
      emplacement: EmplacementRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: EmplacementUpdateComponent,
    resolve: {
      emplacement: EmplacementRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: EmplacementUpdateComponent,
    resolve: {
      emplacement: EmplacementRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(emplacementRoute)],
  exports: [RouterModule],
})
export class EmplacementRoutingModule {}
