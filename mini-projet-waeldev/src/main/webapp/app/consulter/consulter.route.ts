import { Route } from '@angular/router';

import { ConsulterComponent } from 'app/consulter/consulter.component';

export const CONSULTER_ROUTE: Route = {
  path: '',
  component: ConsulterComponent,
  data: {
    pageTitle: 'login.title',
  },
};
