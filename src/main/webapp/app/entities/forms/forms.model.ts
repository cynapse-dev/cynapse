import * as dayjs from 'dayjs';
import { IUser } from 'app/entities/user/user.model';

export interface IForms {
  id?: number;
  name?: string | null;
  cynapseId?: string | null;
  emailId?: string | null;
  phoneNumber?: string | null;
  createdDate?: dayjs.Dayjs | null;
  referalCode?: string | null;
  formDocumentContentType?: string | null;
  formDocument?: string | null;
  user?: IUser | null;
}

export class Forms implements IForms {
  constructor(
    public id?: number,
    public name?: string | null,
    public cynapseId?: string | null,
    public emailId?: string | null,
    public phoneNumber?: string | null,
    public createdDate?: dayjs.Dayjs | null,
    public referalCode?: string | null,
    public formDocumentContentType?: string | null,
    public formDocument?: string | null,
    public user?: IUser | null
  ) {}
}

export function getFormsIdentifier(forms: IForms): number | undefined {
  return forms.id;
}
