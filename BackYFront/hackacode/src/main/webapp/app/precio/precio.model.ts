export class PrecioDTO {

  constructor(data:Partial<PrecioDTO>) {
    Object.assign(this, data);
  }

  id?: number|null;
  idServico?: number|null;
  costo?: number|null;
  precioVenta?: number|null;
  idServicio?: number|null;

}
