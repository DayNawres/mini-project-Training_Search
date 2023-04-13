import dayjs from 'dayjs/esm';
import { ISubscriber } from 'app/entities/subscriber/subscriber.model';
import { Type } from 'app/entities/enumerations/type.model';

export interface IFormation {
  id: number;
  identifiant?: string | null;
  description?: string | null;
  domaine?: string | null;
  type?: Type | null;
  dateDebut?: dayjs.Dayjs | null;
  dateFin?: dayjs.Dayjs | null;
  lesHeuresDeLaFormation?: string | null;
  lePrix?: string | null;
  nomInstructeur?: string | null;
  subscribers?: Pick<ISubscriber, 'id' | 'cIN'>[] | null;
}

export type NewFormation = Omit<IFormation, 'id'> & { id: null };
