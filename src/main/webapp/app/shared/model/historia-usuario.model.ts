import { ITarea } from '@/shared/model/tarea.model';

export interface IHistoriaUsuario {
  id?: number;
  nombre?: string;
  descripcion?: string;
  criteriosAceptacion?: number;
  tareas?: ITarea[] | null;
}

export class HistoriaUsuario implements IHistoriaUsuario {
  constructor(
    public id?: number,
    public nombre?: string,
    public descripcion?: string,
    public criteriosAceptacion?: number,
    public tareas?: ITarea[] | null
  ) {}
}
