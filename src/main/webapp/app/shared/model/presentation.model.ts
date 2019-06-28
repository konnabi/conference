import { Moment } from 'moment';
import { IUser } from 'app/core/user/user.model';

export const enum Rooms {
  A505 = 'A505',
  F328 = 'F328',
  C12 = 'C12',
  B55 = 'B55'
}

export interface IPresentation {
  id?: number;
  presentationName?: string;
  presentationTheme?: string;
  presentationRoom?: Rooms;
  presentationDate?: Moment;
  owners?: IUser[];
}

export class Presentation implements IPresentation {
  constructor(
    public id?: number,
    public presentationName?: string,
    public presentationTheme?: string,
    public presentationRoom?: Rooms,
    public presentationDate?: Moment,
    public owners?: IUser[]
  ) {}
}
