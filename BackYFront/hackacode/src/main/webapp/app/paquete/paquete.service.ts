import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from 'environments/environment';
import { PaqueteDTO } from 'app/paquete/paquete.model';


@Injectable({
  providedIn: 'root',
})
export class PaqueteService {

  http = inject(HttpClient);
  resourcePath = environment.apiPath + '/api/paquetes';

  getAllPaquetes() {
    return this.http.get<PaqueteDTO[]>(this.resourcePath);
  }

  getPaquete(id: number) {
    return this.http.get<PaqueteDTO>(this.resourcePath + '/' + id);
  }

  createPaquete(paqueteDTO: PaqueteDTO) {
    return this.http.post<number>(this.resourcePath, paqueteDTO);
  }

  updatePaquete(id: number, paqueteDTO: PaqueteDTO) {
    return this.http.put<number>(this.resourcePath + '/' + id, paqueteDTO);
  }

  deletePaquete(id: number) {
    return this.http.delete(this.resourcePath + '/' + id);
  }

}
