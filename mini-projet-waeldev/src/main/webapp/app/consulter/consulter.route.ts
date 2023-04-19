import { Route } from '@angular/router';

import { LoginComponent } from './consulter.component';

export const LOGIN_ROUTE: Route = {
  path: '',
  component: LoginComponent,
  data: {
    pageTitle: 'login.title',
  },
};
