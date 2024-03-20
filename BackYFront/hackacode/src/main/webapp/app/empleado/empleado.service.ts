import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from 'environments/environment';
import { EmpleadoDTO } from 'app/empleado/empleado.model';


@Injectable({
  providedIn: 'root',
})
export class EmpleadoService {

  http = inject(HttpClient);
  resourcePath = environment.apiPath + '/api/empleados';

  getAllEmpleadoes() {
    return this.http.get<EmpleadoDTO[]>(this.resourcePath);
  }

  getEmpleado(id: number) {
    return this.http.get<EmpleadoDTO>(this.resourcePath + '/' + id);
  }

  createEmpleado(empleadoDTO: EmpleadoDTO) {
    return this.http.post<number>(this.resourcePath, empleadoDTO);
  }

  updateEmpleado(id: number, empleadoDTO: EmpleadoDTO) {
    return this.http.put<number>(this.resourcePath + '/' + id, empleadoDTO);
  }

  deleteEmpleado(id: number) {
    return this.http.delete(this.resourcePath + '/' + id);
  }

}
