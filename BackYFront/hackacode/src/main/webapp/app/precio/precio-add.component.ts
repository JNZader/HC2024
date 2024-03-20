import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterLink } from '@angular/router';
import { ReactiveFormsModule, FormControl, FormGroup, Validators } from '@angular/forms';
import { InputRowComponent } from 'app/common/input-row/input-row.component';
import { PrecioService } from 'app/precio/precio.service';
import { PrecioDTO } from 'app/precio/precio.model';
import { ErrorHandler } from 'app/common/error-handler.injectable';
import { validDouble } from 'app/common/utils';


@Component({
  selector: 'app-precio-add',
  standalone: true,
  imports: [CommonModule, RouterLink, ReactiveFormsModule, InputRowComponent],
  templateUrl: './precio-add.component.html'
})
export class PrecioAddComponent implements OnInit {

  precioService = inject(PrecioService);
  router = inject(Router);
  errorHandler = inject(ErrorHandler);

  idServicioValues?: Map<number,string>;

  addForm = new FormGroup({
    idServico: new FormControl(null, [Validators.required]),
    costo: new FormControl(null, [Validators.required, validDouble]),
    precioVenta: new FormControl(null, [Validators.required, validDouble]),
    idServicio: new FormControl(null, [Validators.required])
  }, { updateOn: 'submit' });

  getMessage(key: string, details?: any) {
    const messages: Record<string, string> = {
      created: $localize`:@@precio.create.success:Precio was created successfully.`,
      PRECIO_ID_SERVICIO_UNIQUE: $localize`:@@Exists.precio.idServicio:This Servicio is already referenced by another Precio.`
    };
    return messages[key];
  }

  ngOnInit() {
    this.precioService.getIdServicioValues()
        .subscribe({
          next: (data) => this.idServicioValues = data,
          error: (error) => this.errorHandler.handleServerError(error.error)
        });
  }

  handleSubmit() {
    window.scrollTo(0, 0);
    this.addForm.markAllAsTouched();
    if (!this.addForm.valid) {
      return;
    }
    const data = new PrecioDTO(this.addForm.value);
    this.precioService.createPrecio(data)
        .subscribe({
          next: () => this.router.navigate(['/precios'], {
            state: {
              msgSuccess: this.getMessage('created')
            }
          }),
          error: (error) => this.errorHandler.handleServerError(error.error, this.addForm, this.getMessage)
        });
  }

}
