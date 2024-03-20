export class ClienteDTO {

  constructor(data:Partial<ClienteDTO>) {
    Object.assign(this, data);
  }

  id?: number|null;
  nombre?: string|null;
  apellido?: string|null;
  direccion?: string|null;
  dni?: string|null;
  fechaNacimiento?: string|null;
  nacionalidad?: string|null;
  celular?: string|null;
  email?: string|null;
  estado?: boolean|null;

}
