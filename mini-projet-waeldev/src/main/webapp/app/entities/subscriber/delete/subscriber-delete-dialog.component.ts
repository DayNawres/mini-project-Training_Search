import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ISubscriber } from '../subscriber.model';
import { SubscriberService } from '../service/subscriber.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './subscriber-delete-dialog.component.html',
})
export class SubscriberDeleteDialogComponent {
  subscriber?: ISubscriber;

  constructor(protected subscriberService: SubscriberService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.subscriberService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
