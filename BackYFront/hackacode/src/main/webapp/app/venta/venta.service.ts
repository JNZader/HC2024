import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from 'environments/environment';
import { VentaDTO } from 'app/venta/venta.model';
import { map } from 'rxjs';
import { transformRecordToMap } from 'app/common/utils';


@Injectable({
  providedIn: 'root',
})
export class VentaService {

  http = inject(HttpClient);
  resourcePath = environment.apiPath + '/api/ventas';

  getAllVentas() {
    return this.http.get<VentaDTO[]>(this.resourcePath);
  }

  getVenta(id: number) {
    return this.http.get<VentaDTO>(this.resourcePath + '/' + id);
  }

  createVenta(ventaDTO: VentaDTO) {
    return this.http.post<number>(this.resourcePath, ventaDTO);
  }

  updateVenta(id: number, ventaDTO: VentaDTO) {
    return this.http.put<number>(this.resourcePath + '/' + id, ventaDTO);
  }

  deleteVenta(id: number) {
    return this.http.delete(this.resourcePath + '/' + id);
  }

  getClienteIdValues() {
    return this.http.get<Record<string,number>>(this.resourcePath + '/clienteIdValues')
        .pipe(map(transformRecordToMap));
  }

  getEmpleadoidValues() {
    return this.http.get<Record<string,number>>(this.resourcePath + '/empleadoidValues')
        .pipe(map(transformRecordToMap));
  }

  getPaquetesValues() {
    return this.http.get<Record<string,number>>(this.resourcePath + '/paquetesValues')
        .pipe(map(transformRecordToMap));
  }

}
