import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from 'environments/environment';
import { PrecioDTO } from 'app/precio/precio.model';
import { map } from 'rxjs';
import { transformRecordToMap } from 'app/common/utils';


@Injectable({
  providedIn: 'root',
})
export class PrecioService {

  http = inject(HttpClient);
  resourcePath = environment.apiPath + '/api/precios';

  getAllPrecios() {
    return this.http.get<PrecioDTO[]>(this.resourcePath);
  }

  getPrecio(id: number) {
    return this.http.get<PrecioDTO>(this.resourcePath + '/' + id);
  }

  createPrecio(precioDTO: PrecioDTO) {
    return this.http.post<number>(this.resourcePath, precioDTO);
  }

  updatePrecio(id: number, precioDTO: PrecioDTO) {
    return this.http.put<number>(this.resourcePath + '/' + id, precioDTO);
  }

  deletePrecio(id: number) {
    return this.http.delete(this.resourcePath + '/' + id);
  }

  getIdServicioValues() {
    return this.http.get<Record<string,number>>(this.resourcePath + '/idServicioValues')
        .pipe(map(transformRecordToMap));
  }

}
