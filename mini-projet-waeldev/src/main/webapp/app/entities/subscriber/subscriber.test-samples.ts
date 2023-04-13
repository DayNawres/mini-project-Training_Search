import { ISubscriber, NewSubscriber } from './subscriber.model';

export const sampleWithRequiredData: ISubscriber = {
  id: 83730,
  cIN: 'Account Avon',
  nom: 'eyeballs',
  prenom: 'Market',
  statut: 'next calculating',
};

export const sampleWithPartialData: ISubscriber = {
  id: 40370,
  cIN: 'deposit',
  nom: 'SMTP payment Frozen',
  prenom: 'c Steel systematic',
  age: 5264,
  statut: 'Configurable application',
};

export const sampleWithFullData: ISubscriber = {
  id: 99874,
  cIN: 'frictionless Centre',
  nom: 'TCP Cambridgeshire hard',
  prenom: 'cutting-edge Practical',
  age: 21914,
  statut: 'Tools',
};

export const sampleWithNewData: NewSubscriber = {
  cIN: 'functionalities',
  nom: 'c Buckinghamshire a',
  prenom: 'array indigo alarm',
  statut: 'New',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
