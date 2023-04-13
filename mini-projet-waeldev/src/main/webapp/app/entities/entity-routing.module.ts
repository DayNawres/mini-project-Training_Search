import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'formation',
        data: { pageTitle: 'digiparcApp.formation.home.title' },
        loadChildren: () => import('./formation/formation.module').then(m => m.FormationModule),
      },
      {
        path: 'emplacement',
        data: { pageTitle: 'digiparcApp.emplacement.home.title' },
        loadChildren: () => import('./emplacement/emplacement.module').then(m => m.EmplacementModule),
      },
      {
        path: 'inscription',
        data: { pageTitle: 'digiparcApp.inscription.home.title' },
        loadChildren: () => import('./inscription/inscription.module').then(m => m.InscriptionModule),
      },
      {
        path: 'subscriber',
        data: { pageTitle: 'digiparcApp.subscriber.home.title' },
        loadChildren: () => import('./subscriber/subscriber.module').then(m => m.SubscriberModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
