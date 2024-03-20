import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterLink } from '@angular/router';
import { ReactiveFormsModule, FormControl, FormGroup, Validators } from '@angular/forms';
import { InputRowComponent } from 'app/common/input-row/input-row.component';
import { VentaService } from 'app/venta/venta.service';
import { VentaDTO } from 'app/venta/venta.model';
import { ErrorHandler } from 'app/common/error-handler.injectable';
import { validDouble } from 'app/common/utils';


@Component({
  selector: 'app-venta-add',
  standalone: true,
  imports: [CommonModule, RouterLink, ReactiveFormsModule, InputRowComponent],
  templateUrl: './venta-add.component.html'
})
export class VentaAddComponent implements OnInit {

  ventaService = inject(VentaService);
  router = inject(Router);
  errorHandler = inject(ErrorHandler);

  clienteIdValues?: Map<number,string>;
  empleadoidValues?: Map<number,string>;
  paquetesValues?: Map<number,string>;

  addForm = new FormGroup({
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
      created: $localize`:@@venta.create.success:Venta was created successfully.`
    };
    return messages[key];
  }

  ngOnInit() {
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
  }

  handleSubmit() {
    window.scrollTo(0, 0);
    this.addForm.markAllAsTouched();
    if (!this.addForm.valid) {
      return;
    }
    const data = new VentaDTO(this.addForm.value);
    this.ventaService.createVenta(data)
        .subscribe({
          next: () => this.router.navigate(['/ventas'], {
            state: {
              msgSuccess: this.getMessage('created')
            }
          }),
          error: (error) => this.errorHandler.handleServerError(error.error, this.addForm, this.getMessage)
        });
  }

}
