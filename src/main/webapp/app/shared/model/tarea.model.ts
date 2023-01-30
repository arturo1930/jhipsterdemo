import { IHistoriaUsuario } from '@/shared/model/historia-usuario.model';

export interface ITarea {
  id?: number;
  nombre?: string;
  actividades?: string | null;
  fechaCreacion?: Date | null;
  historiaUsuario?: IHistoriaUsuario | null;
}

export class Tarea implements ITarea {
  constructor(
    public id?: number,
    public nombre?: string,
    public actividades?: string | null,
    public fechaCreacion?: Date | null,
    public historiaUsuario?: IHistoriaUsuario | null
  ) {}
}
