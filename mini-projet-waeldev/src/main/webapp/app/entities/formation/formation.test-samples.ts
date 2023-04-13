import dayjs from 'dayjs/esm';

import { Type } from 'app/entities/enumerations/type.model';

import { IFormation, NewFormation } from './formation.model';

export const sampleWithRequiredData: IFormation = {
  id: 7564,
  identifiant: 'Norwegian overriding Uruguayo',
  description: 'PCI',
  dateDebut: dayjs('2023-04-02'),
  dateFin: dayjs('2023-04-02'),
  lesHeuresDeLaFormation: 'capacitor Rhône-Alpes',
};

export const sampleWithPartialData: IFormation = {
  id: 79654,
  identifiant: 'value-added Bourgogne ADP',
  description: 'Grands Wooden JSON',
  dateDebut: dayjs('2023-04-02'),
  dateFin: dayjs('2023-04-02'),
  lesHeuresDeLaFormation: 'incremental',
};

export const sampleWithFullData: IFormation = {
  id: 56341,
  identifiant: 'c a la',
  description: 'green Suriname CSS',
  domaine: 'complexity',
  type: Type['EnLigne'],
  dateDebut: dayjs('2023-04-02'),
  dateFin: dayjs('2023-04-02'),
  lesHeuresDeLaFormation: 'Sri c',
  lePrix: 'Corse Languedoc-Roussillon',
  nomInstructeur: 'withdrawal Mouse drive',
};

export const sampleWithNewData: NewFormation = {
  identifiant: 'benchmark benchmark',
  description: 'a copy',
  dateDebut: dayjs('2023-04-02'),
  dateFin: dayjs('2023-04-02'),
  lesHeuresDeLaFormation: 'bandwidth real-time application',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
