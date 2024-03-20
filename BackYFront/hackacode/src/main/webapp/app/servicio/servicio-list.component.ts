import { Component, inject, OnDestroy, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NavigationEnd, Router, RouterLink } from '@angular/router';
import { Subscription } from 'rxjs';
import { ErrorHandler } from 'app/common/error-handler.injectable';
import { ServicioService } from 'app/servicio/servicio.service';
import { ServicioDTO } from 'app/servicio/servicio.model';


@Component({
  selector: 'app-servicio-list',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './servicio-list.component.html'})
export class ServicioListComponent implements OnInit, OnDestroy {

  servicioService = inject(ServicioService);
  errorHandler = inject(ErrorHandler);
  router = inject(Router);
  servicios?: ServicioDTO[];
  navigationSubscription?: Subscription;

  getMessage(key: string, details?: any) {
    const messages: Record<string, string> = {
      confirm: $localize`:@@delete.confirm:Do you really want to delete this element? This cannot be undone.`,
      deleted: $localize`:@@servicio.delete.success:Servicio was removed successfully.`,
      'servicio.precio.idServicio.referenced': $localize`:@@servicio.precio.idServicio.referenced:This entity is still referenced by Precio ${details?.id} via field Id Servicio.`
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
    this.servicioService.getAllServicios()
        .subscribe({
          next: (data) => this.servicios = data,
          error: (error) => this.errorHandler.handleServerError(error.error)
        });
  }

  confirmDelete(id: number) {
    if (confirm(this.getMessage('confirm'))) {
      this.servicioService.deleteServicio(id)
          .subscribe({
            next: () => this.router.navigate(['/servicios'], {
              state: {
                msgInfo: this.getMessage('deleted')
              }
            }),
            error: (error) => {
              if (error.error?.code === 'REFERENCED') {
                const messageParts = error.error.message.split(',');
                this.router.navigate(['/servicios'], {
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
