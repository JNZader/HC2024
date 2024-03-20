import { Component, inject, OnDestroy, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NavigationEnd, Router, RouterLink } from '@angular/router';
import { Subscription } from 'rxjs';
import { ErrorHandler } from 'app/common/error-handler.injectable';
import { PrecioService } from 'app/precio/precio.service';
import { PrecioDTO } from 'app/precio/precio.model';


@Component({
  selector: 'app-precio-list',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './precio-list.component.html'})
export class PrecioListComponent implements OnInit, OnDestroy {

  precioService = inject(PrecioService);
  errorHandler = inject(ErrorHandler);
  router = inject(Router);
  precios?: PrecioDTO[];
  navigationSubscription?: Subscription;

  getMessage(key: string, details?: any) {
    const messages: Record<string, string> = {
      confirm: $localize`:@@delete.confirm:Do you really want to delete this element? This cannot be undone.`,
      deleted: $localize`:@@precio.delete.success:Precio was removed successfully.`    };
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
    this.precioService.getAllPrecios()
        .subscribe({
          next: (data) => this.precios = data,
          error: (error) => this.errorHandler.handleServerError(error.error)
        });
  }

  confirmDelete(id: number) {
    if (confirm(this.getMessage('confirm'))) {
      this.precioService.deletePrecio(id)
          .subscribe({
            next: () => this.router.navigate(['/precios'], {
              state: {
                msgInfo: this.getMessage('deleted')
              }
            }),
            error: (error) => this.errorHandler.handleServerError(error.error)
          });
    }
  }

}
