import { IInscription } from 'app/entities/inscription/inscription.model';
import { IFormation } from 'app/entities/formation/formation.model';

export interface ISubscriber {
  id: number;
  cIN?: string | null;
  nom?: string | null;
  prenom?: string | null;
  age?: number | null;
  statut?: string | null;
  inscription?: Pick<IInscription, 'id'> | null;
  formations?: Pick<IFormation, 'id' | 'identifiant'>[] | null;
}

export type NewSubscriber = Omit<ISubscriber, 'id'> & { id: null };
