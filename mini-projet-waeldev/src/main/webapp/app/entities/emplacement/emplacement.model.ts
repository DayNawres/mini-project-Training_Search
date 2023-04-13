import { IFormation } from 'app/entities/formation/formation.model';

export interface IEmplacement {
  id: number;
  identifiant?: number | null;
  type?: string | null;
  centreDeFormation?: string | null;
  webSiteLink?: string | null;
  adresse?: string | null;
  formation?: Pick<IFormation, 'id'> | null;
}

export type NewEmplacement = Omit<IEmplacement, 'id'> & { id: null };
