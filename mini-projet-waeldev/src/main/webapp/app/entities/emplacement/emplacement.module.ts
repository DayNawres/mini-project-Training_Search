import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { EmplacementComponent } from './list/emplacement.component';
import { EmplacementDetailComponent } from './detail/emplacement-detail.component';
import { EmplacementUpdateComponent } from './update/emplacement-update.component';
import { EmplacementDeleteDialogComponent } from './delete/emplacement-delete-dialog.component';
import { EmplacementRoutingModule } from './route/emplacement-routing.module';

@NgModule({
  imports: [SharedModule, EmplacementRoutingModule],
  declarations: [EmplacementComponent, EmplacementDetailComponent, EmplacementUpdateComponent, EmplacementDeleteDialogComponent],
})
export class EmplacementModule {}
