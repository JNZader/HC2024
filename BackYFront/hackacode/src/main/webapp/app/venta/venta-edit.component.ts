import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { ReactiveFormsModule, FormControl, FormGroup, Validators } from '@angular/forms';
import { InputRowComponent } from 'app/common/input-row/input-row.component';
import { VentaService } from 'app/venta/venta.service';
import { VentaDTO } from 'app/venta/venta.model';
import { ErrorHandler } from 'app/common/error-handler.injectable';
import { updateForm, validDouble } from 'app/common/utils';


@Component({
  selector: 'app-venta-edit',
  standalone: true,
  imports: [CommonModule, RouterLink, ReactiveFormsModule, InputRowComponent],
  templateUrl: './venta-edit.component.html'
})
export class VentaEditComponent implements OnInit {

  ventaService = inject(VentaService);
  route = inject(ActivatedRoute);
  router = inject(Router);
  errorHandler = inject(ErrorHandler);

  clienteIdValues?: Map<number,string>;
  empleadoidValues?: Map<number,string>;
  paquetesValues?: Map<number,string>;
  currentId?: number;

  editForm = new FormGroup({
    id: new FormControl({ value: null, disabled: true }),
    medioPago: new FormControl(null, [Validators.required]),
    monto: new FormControl(null, [validDouble]),
    fecha: new FormControl(null, [Validators.required]),
    estado: new FormControl(false),
    clienteId: new FormControl(null),
    empleadoid: new FormControl(null),
    paquetes: new FormControl([])
  }, { updateOn: 'submit' });

  getMessage(key: string, details?: any) {
    const messages: Record<string, string> = {
      updated: $localize`:@@venta.update.success:Venta was updated successfully.`
    };
    return messages[key];
  }

  ngOnInit() {
    this.currentId = +this.route.snapshot.params['id'];
    this.ventaService.getClienteIdValues()
        .subscribe({
          next: (data) => this.clienteIdValues = data,
          error: (error) => this.errorHandler.handleServerError(error.error)
        });
    this.ventaService.getEmpleadoidValues()
        .subscribe({
          next: (data) => this.empleadoidValues = data,
          error: (error) => this.errorHandler.handleServerError(error.error)
        });
    this.ventaService.getPaquetesValues()
        .subscribe({
          next: (data) => this.paquetesValues = data,
          error: (error) => this.errorHandler.handleServerError(error.error)
        });
    this.ventaService.getVenta(this.currentId!)
        .subscribe({
          next: (data) => updateForm(this.editForm, data),
          error: (error) => this.errorHandler.handleServerError(error.error)
        });
  }

  handleSubmit() {
    window.scrollTo(0, 0);
    this.editForm.markAllAsTouched();
    if (!this.editForm.valid) {
      return;
    }
    const data = new VentaDTO(this.editForm.value);
    this.ventaService.updateVenta(this.currentId!, data)
        .subscribe({
          next: () => this.router.navigate(['/ventas'], {
            state: {
              msgSuccess: this.getMessage('updated')
            }
          }),
          error: (error) => this.errorHandler.handleServerError(error.error, this.editForm, this.getMessage)
        });
  }

}
