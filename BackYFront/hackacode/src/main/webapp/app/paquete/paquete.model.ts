export class PaqueteDTO {

  constructor(data:Partial<PaqueteDTO>) {
    Object.assign(this, data);
  }

  id?: number|null;
  tipo?: string|null;
  nombre?: string|null;
  descripcionBreve?: string|null;
  precioVenta?: number|null;
  duracion?: number|null;
  estado?: boolean|null;

}
