import { Component, inject, OnDestroy, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NavigationEnd, Router, RouterLink } from '@angular/router';
import { Subscription } from 'rxjs';
import { ErrorHandler } from 'app/common/error-handler.injectable';
import { VentaService } from 'app/venta/venta.service';
import { VentaDTO } from 'app/venta/venta.model';


@Component({
  selector: 'app-venta-list',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './venta-list.component.html'})
export class VentaListComponent implements OnInit, OnDestroy {

  ventaService = inject(VentaService);
  errorHandler = inject(ErrorHandler);
  router = inject(Router);
  ventas?: VentaDTO[];
  navigationSubscription?: Subscription;

  getMessage(key: string, details?: any) {
    const messages: Record<string, string> = {
      confirm: $localize`:@@delete.confirm:Do you really want to delete this element? This cannot be undone.`,
      deleted: $localize`:@@venta.delete.success:Venta was removed successfully.`    };
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
    this.ventaService.getAllVentas()
        .subscribe({
          next: (data) => this.ventas = data,
          error: (error) => this.errorHandler.handleServerError(error.error)
        });
  }

  confirmDelete(id: number) {
    if (confirm(this.getMessage('confirm'))) {
      this.ventaService.deleteVenta(id)
          .subscribe({
            next: () => this.router.navigate(['/ventas'], {
              state: {
                msgInfo: this.getMessage('deleted')
              }
            }),
            error: (error) => this.errorHandler.handleServerError(error.error)
          });
    }
  }

}
