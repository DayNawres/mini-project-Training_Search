import { IEmplacement, NewEmplacement } from './emplacement.model';

export const sampleWithRequiredData: IEmplacement = {
  id: 41422,
  identifiant: 58444,
  type: 'open-source synthesize',
};

export const sampleWithPartialData: IEmplacement = {
  id: 26864,
  identifiant: 17380,
  type: 'Clothing',
  adresse: 'Superviseur Auvergne',
};

export const sampleWithFullData: IEmplacement = {
  id: 92065,
  identifiant: 46902,
  type: 'Chair Hat',
  centreDeFormation: 'Keyboard schemas',
  webSiteLink: 'payment ADP',
  adresse: 'Account',
};

export const sampleWithNewData: NewEmplacement = {
  identifiant: 74885,
  type: 'Movies Bacon Steel',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
