import { Component, inject, OnDestroy, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NavigationEnd, Router, RouterLink } from '@angular/router';
import { Subscription } from 'rxjs';
import { ErrorHandler } from 'app/common/error-handler.injectable';
import { EmpleadoService } from 'app/empleado/empleado.service';
import { EmpleadoDTO } from 'app/empleado/empleado.model';


@Component({
  selector: 'app-empleado-list',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './empleado-list.component.html'})
export class EmpleadoListComponent implements OnInit, OnDestroy {

  empleadoService = inject(EmpleadoService);
  errorHandler = inject(ErrorHandler);
  router = inject(Router);
  empleadoes?: EmpleadoDTO[];
  navigationSubscription?: Subscription;

  getMessage(key: string, details?: any) {
    const messages: Record<string, string> = {
      confirm: $localize`:@@delete.confirm:Do you really want to delete this element? This cannot be undone.`,
      deleted: $localize`:@@empleado.delete.success:Empleado was removed successfully.`,
      'empleado.venta.empleadoid.referenced': $localize`:@@empleado.venta.empleadoid.referenced:This entity is still referenced by Venta ${details?.id} via field Empleadoid.`
    };
    return messages[key];
  }

  ngOnInit() {
    this.loadData();
    this.navigationSubscription = this.router.events.subscribe((event) => {
      if (event instanceof NavigationEnd) {
        this.loadData();
      }
    });
  }

  ngOnDestroy() {
    this.navigationSubscription!.unsubscribe();
  }
  
  loadData() {
    this.empleadoService.getAllEmpleadoes()
        .subscribe({
          next: (data) => this.empleadoes = data,
          error: (error) => this.errorHandler.handleServerError(error.error)
        });
  }

  confirmDelete(id: number) {
    if (confirm(this.getMessage('confirm'))) {
      this.empleadoService.deleteEmpleado(id)
          .subscribe({
            next: () => this.router.navigate(['/empleados'], {
              state: {
                msgInfo: this.getMessage('deleted')
              }
            }),
            error: (error) => {
              if (error.error?.code === 'REFERENCED') {
                const messageParts = error.error.message.split(',');
                this.router.navigate(['/empleados'], {
                  state: {
                    msgError: this.getMessage(messageParts[0], { id: messageParts[1] })
                  }
                });
                return;
              }
              this.errorHandler.handleServerError(error.error)
            }
          });
    }
  }

}
