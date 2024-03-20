export class VentaDTO {

  constructor(data:Partial<VentaDTO>) {
    Object.assign(this, data);
  }

  id?: number|null;
  medioPago?: string|null;
  monto?: number|null;
  fecha?: string|null;
  estado?: boolean|null;
  clienteId?: number|null;
  empleadoid?: number|null;
  paquetes?: number[]|null;

}
