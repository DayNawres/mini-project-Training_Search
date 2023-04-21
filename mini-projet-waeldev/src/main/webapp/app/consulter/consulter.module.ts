import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SharedModule } from 'app/shared/shared.module';
import { CONSULTER_ROUTE } from 'app/consulter/consulter.route';
import { ConsulterComponent } from 'app/consulter/consulter.component';

@NgModule({
  imports: [SharedModule, RouterModule.forChild([CONSULTER_ROUTE])],
  declarations: [ConsulterComponent],
})
export class ConsulterModule {}
