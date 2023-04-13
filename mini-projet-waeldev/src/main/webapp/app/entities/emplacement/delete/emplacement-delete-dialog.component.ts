import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IEmplacement } from '../emplacement.model';
import { EmplacementService } from '../service/emplacement.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './emplacement-delete-dialog.component.html',
})
export class EmplacementDeleteDialogComponent {
  emplacement?: IEmplacement;

  constructor(protected emplacementService: EmplacementService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.emplacementService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
