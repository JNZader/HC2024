export class ServicioDTO {

  constructor(data:Partial<ServicioDTO>) {
    Object.assign(this, data);
  }

  id?: number|null;
  nombre?: string|null;
  descripcion?: string|null;
  fecha?: string|null;
  duracion?: number|null;
  estado?: boolean|null;
  paqueteid?: number|null;

}
