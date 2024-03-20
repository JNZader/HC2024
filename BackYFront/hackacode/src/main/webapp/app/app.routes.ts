import { Routes } from '@angular/router';
import { HomeComponent } from './home/home.component';
import { EmpleadoListComponent } from './empleado/empleado-list.component';
import { EmpleadoAddComponent } from './empleado/empleado-add.component';
import { EmpleadoEditComponent } from './empleado/empleado-edit.component';
import { ClienteListComponent } from './cliente/cliente-list.component';
import { ClienteAddComponent } from './cliente/cliente-add.component';
import { ClienteEditComponent } from './cliente/cliente-edit.component';
import { PaqueteListComponent } from './paquete/paquete-list.component';
import { PaqueteAddComponent } from './paquete/paquete-add.component';
import { PaqueteEditComponent } from './paquete/paquete-edit.component';
import { ServicioListComponent } from './servicio/servicio-list.component';
import { ServicioAddComponent } from './servicio/servicio-add.component';
import { ServicioEditComponent } from './servicio/servicio-edit.component';
import { VentaListComponent } from './venta/venta-list.component';
import { VentaAddComponent } from './venta/venta-add.component';
import { VentaEditComponent } from './venta/venta-edit.component';
import { PrecioListComponent } from './precio/precio-list.component';
import { PrecioAddComponent } from './precio/precio-add.component';
import { PrecioEditComponent } from './precio/precio-edit.component';
import { ErrorComponent } from './error/error.component';


export const routes: Routes = [
  {
    path: '',
    component: HomeComponent,
    title: $localize`:@@home.index.headline:Welcome to your new app!`
  },
  {
    path: 'empleados',
    component: EmpleadoListComponent,
    title: $localize`:@@empleado.list.headline:Empleadoes`
  },
  {
    path: 'empleados/add',
    component: EmpleadoAddComponent,
    title: $localize`:@@empleado.add.headline:Add Empleado`
  },
  {
    path: 'empleados/edit/:id',
    component: EmpleadoEditComponent,
    title: $localize`:@@empleado.edit.headline:Edit Empleado`
  },
  {
    path: 'clientes',
    component: ClienteListComponent,
    title: $localize`:@@cliente.list.headline:Clientes`
  },
  {
    path: 'clientes/add',
    component: ClienteAddComponent,
    title: $localize`:@@cliente.add.headline:Add Cliente`
  },
  {
    path: 'clientes/edit/:id',
    component: ClienteEditComponent,
    title: $localize`:@@cliente.edit.headline:Edit Cliente`
  },
  {
    path: 'paquetes',
    component: PaqueteListComponent,
    title: $localize`:@@paquete.list.headline:Paquetes`
  },
  {
    path: 'paquetes/add',
    component: PaqueteAddComponent,
    title: $localize`:@@paquete.add.headline:Add Paquete`
  },
  {
    path: 'paquetes/edit/:id',
    component: PaqueteEditComponent,
    title: $localize`:@@paquete.edit.headline:Edit Paquete`
  },
  {
    path: 'servicios',
    component: ServicioListComponent,
    title: $localize`:@@servicio.list.headline:Servicios`
  },
  {
    path: 'servicios/add',
    component: ServicioAddComponent,
    title: $localize`:@@servicio.add.headline:Add Servicio`
  },
  {
    path: 'servicios/edit/:id',
    component: ServicioEditComponent,
    title: $localize`:@@servicio.edit.headline:Edit Servicio`
  },
  {
    path: 'ventas',
    component: VentaListComponent,
    title: $localize`:@@venta.list.headline:Ventas`
  },
  {
    path: 'ventas/add',
    component: VentaAddComponent,
    title: $localize`:@@venta.add.headline:Add Venta`
  },
  {
    path: 'ventas/edit/:id',
    component: VentaEditComponent,
    title: $localize`:@@venta.edit.headline:Edit Venta`
  },
  {
    path: 'precios',
    component: PrecioListComponent,
    title: $localize`:@@precio.list.headline:Precios`
  },
  {
    path: 'precios/add',
    component: PrecioAddComponent,
    title: $localize`:@@precio.add.headline:Add Precio`
  },
  {
    path: 'precios/edit/:id',
    component: PrecioEditComponent,
    title: $localize`:@@precio.edit.headline:Edit Precio`
  },
  {
    path: 'error',
    component: ErrorComponent,
    title: $localize`:@@error.headline:Error`
  },
  {
    path: '**',
    component: ErrorComponent,
    title: $localize`:@@notFound.headline:Page not found`
  }
];
