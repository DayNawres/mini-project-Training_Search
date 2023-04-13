import dayjs from 'dayjs/esm';

import { IInscription, NewInscription } from './inscription.model';

export const sampleWithRequiredData: IInscription = {
  id: 45715,
  objet: 'payment haptic',
  dateValiditeDebut: dayjs('2023-04-02'),
  dateValiditeFin: dayjs('2023-04-02'),
};

export const sampleWithPartialData: IInscription = {
  id: 79470,
  objet: 'Account',
  dateValiditeDebut: dayjs('2023-04-02'),
  dateValiditeFin: dayjs('2023-04-02'),
};

export const sampleWithFullData: IInscription = {
  id: 31963,
  objet: 'Savings',
  dateValiditeDebut: dayjs('2023-04-02'),
  dateValiditeFin: dayjs('2023-04-02'),
};

export const sampleWithNewData: NewInscription = {
  objet: 'Iranian',
  dateValiditeDebut: dayjs('2023-04-02'),
  dateValiditeFin: dayjs('2023-04-02'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
