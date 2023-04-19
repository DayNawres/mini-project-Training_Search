import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { FormationComponent } from 'app/entities/formation/list/formation.component';
import { FormationDetailComponent } from 'app/entities/formation/detail/formation-detail.component';
import { FormationUpdateComponent } from 'app/entities/formation//update/formation-update.component';
import { FormationRoutingModule } from 'app/entities/formation/route/formation-routing.module';

@NgModule({
  imports: [SharedModule, FormationRoutingModule],
  declarations: [FormationComponent, FormationDetailComponent, FormationUpdateComponent],
})
export class FormationModule {}
