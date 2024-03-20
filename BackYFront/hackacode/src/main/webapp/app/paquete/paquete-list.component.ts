import { Component, inject, OnDestroy, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NavigationEnd, Router, RouterLink } from '@angular/router';
import { Subscription } from 'rxjs';
import { ErrorHandler } from 'app/common/error-handler.injectable';
import { PaqueteService } from 'app/paquete/paquete.service';
import { PaqueteDTO } from 'app/paquete/paquete.model';


@Component({
  selector: 'app-paquete-list',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './paquete-list.component.html'})
export class PaqueteListComponent implements OnInit, OnDestroy {

  paqueteService = inject(PaqueteService);
  errorHandler = inject(ErrorHandler);
  router = inject(Router);
  paquetes?: PaqueteDTO[];
  navigationSubscription?: Subscription;

  getMessage(key: string, details?: any) {
    const messages: Record<string, string> = {
      confirm: $localize`:@@delete.confirm:Do you really want to delete this element? This cannot be undone.`,
      deleted: $localize`:@@paquete.delete.success:Paquete was removed successfully.`,
      'paquete.servicio.paqueteid.referenced': $localize`:@@paquete.servicio.paqueteid.referenced:This entity is still referenced by Servicio ${details?.id} via field Paqueteid.`,
      'paquete.venta.paquetes.referenced': $localize`:@@paquete.venta.paquetes.referenced:This entity is still referenced by Venta ${details?.id} via field Paquetes.`
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
    this.paqueteService.getAllPaquetes()
        .subscribe({
          next: (data) => this.paquetes = data,
          error: (error) => this.errorHandler.handleServerError(error.error)
        });
  }

  confirmDelete(id: number) {
    if (confirm(this.getMessage('confirm'))) {
      this.paqueteService.deletePaquete(id)
          .subscribe({
            next: () => this.router.navigate(['/paquetes'], {
              state: {
                msgInfo: this.getMessage('deleted')
              }
            }),
            error: (error) => {
              if (error.error?.code === 'REFERENCED') {
                const messageParts = error.error.message.split(',');
                this.router.navigate(['/paquetes'], {
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
