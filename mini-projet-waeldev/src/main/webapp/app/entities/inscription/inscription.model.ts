import dayjs from 'dayjs/esm';
import { IFormation } from 'app/entities/formation/formation.model';

export interface IInscription {
  id: number;
  objet?: string | null;
  dateValiditeDebut?: dayjs.Dayjs | null;
  dateValiditeFin?: dayjs.Dayjs | null;
  formation?: Pick<IFormation, 'id' | 'identifiant'> | null;
}

export type NewInscription = Omit<IInscription, 'id'> & { id: null };
