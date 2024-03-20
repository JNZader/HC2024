import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { ReactiveFormsModule, FormControl, FormGroup, Validators } from '@angular/forms';
import { InputRowComponent } from 'app/common/input-row/input-row.component';
import { PrecioService } from 'app/precio/precio.service';
import { PrecioDTO } from 'app/precio/precio.model';
import { ErrorHandler } from 'app/common/error-handler.injectable';
import { updateForm, validDouble } from 'app/common/utils';


@Component({
  selector: 'app-precio-edit',
  standalone: true,
  imports: [CommonModule, RouterLink, ReactiveFormsModule, InputRowComponent],
  templateUrl: './precio-edit.component.html'
})
export class PrecioEditComponent implements OnInit {

  precioService = inject(PrecioService);
  route = inject(ActivatedRoute);
  router = inject(Router);
  errorHandler = inject(ErrorHandler);

  idServicioValues?: Map<number,string>;
  currentId?: number;

  editForm = new FormGroup({
    id: new FormControl({ value: null, disabled: true }),
    idServico: new FormControl(null, [Validators.required]),
    costo: new FormControl(null, [Validators.required, validDouble]),
    precioVenta: new FormControl(null, [Validators.required, validDouble]),
    idServicio: new FormControl(null, [Validators.required])
  }, { updateOn: 'submit' });

  getMessage(key: string, details?: any) {
    const messages: Record<string, string> = {
      updated: $localize`:@@precio.update.success:Precio was updated successfully.`,
      PRECIO_ID_SERVICIO_UNIQUE: $localize`:@@Exists.precio.idServicio:This Servicio is already referenced by another Precio.`
    };
    return messages[key];
  }

  ngOnInit() {
    this.currentId = +this.route.snapshot.params['id'];
    this.precioService.getIdServicioValues()
        .subscribe({
          next: (data) => this.idServicioValues = data,
          error: (error) => this.errorHandler.handleServerError(error.error)
        });
    this.precioService.getPrecio(this.currentId!)
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
    const data = new PrecioDTO(this.editForm.value);
    this.precioService.updatePrecio(this.currentId!, data)
        .subscribe({
          next: () => this.router.navigate(['/precios'], {
            state: {
              msgSuccess: this.getMessage('updated')
            }
          }),
          error: (error) => this.errorHandler.handleServerError(error.error, this.editForm, this.getMessage)
        });
  }

}
