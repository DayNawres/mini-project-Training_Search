import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { SubscriberComponent } from './list/subscriber.component';
import { SubscriberDetailComponent } from './detail/subscriber-detail.component';
import { SubscriberUpdateComponent } from './update/subscriber-update.component';
import { SubscriberDeleteDialogComponent } from './delete/subscriber-delete-dialog.component';
import { SubscriberRoutingModule } from './route/subscriber-routing.module';

@NgModule({
  imports: [SharedModule, SubscriberRoutingModule],
  declarations: [SubscriberComponent, SubscriberDetailComponent, SubscriberUpdateComponent, SubscriberDeleteDialogComponent],
})
export class SubscriberModule {}
