export class EmpleadoDTO {

  constructor(data:Partial<EmpleadoDTO>) {
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
  cargo?: string|null;
  sueldo?: number|null;
  estado?: boolean|null;

}
