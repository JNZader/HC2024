import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from 'environments/environment';
import { ServicioDTO } from 'app/servicio/servicio.model';
import { map } from 'rxjs';
import { transformRecordToMap } from 'app/common/utils';


@Injectable({
  providedIn: 'root',
})
export class ServicioService {

  http = inject(HttpClient);
  resourcePath = environment.apiPath + '/api/servicios';

  getAllServicios() {
    return this.http.get<ServicioDTO[]>(this.resourcePath);
  }

  getServicio(id: number) {
    return this.http.get<ServicioDTO>(this.resourcePath + '/' + id);
  }

  createServicio(servicioDTO: ServicioDTO) {
    return this.http.post<number>(this.resourcePath, servicioDTO);
  }

  updateServicio(id: number, servicioDTO: ServicioDTO) {
    return this.http.put<number>(this.resourcePath + '/' + id, servicioDTO);
  }

  deleteServicio(id: number) {
    return this.http.delete(this.resourcePath + '/' + id);
  }

  getPaqueteidValues() {
    return this.http.get<Record<string,number>>(this.resourcePath + '/paqueteidValues')
        .pipe(map(transformRecordToMap));
  }

}
