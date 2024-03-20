import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { ReactiveFormsModule, FormControl, FormGroup, Validators } from '@angular/forms';
import { InputRowComponent } from 'app/common/input-row/input-row.component';
import { ServicioService } from 'app/servicio/servicio.service';
import { ServicioDTO } from 'app/servicio/servicio.model';
import { ErrorHandler } from 'app/common/error-handler.injectable';
import { updateForm } from 'app/common/utils';


@Component({
  selector: 'app-servicio-edit',
  standalone: true,
  imports: [CommonModule, RouterLink, ReactiveFormsModule, InputRowComponent],
  templateUrl: './servicio-edit.component.html'
})
export class ServicioEditComponent implements OnInit {

  servicioService = inject(ServicioService);
  route = inject(ActivatedRoute);
  router = inject(Router);
  errorHandler = inject(ErrorHandler);

  paqueteidValues?: Map<number,string>;
  currentId?: number;

  editForm = new FormGroup({
    id: new FormControl({ value: null, disabled: true }),
    nombre: new FormControl(null, [Validators.maxLength(255)]),
    descripcion: new FormControl(null, [Validators.maxLength(255)]),
    fecha: new FormControl(null),
    duracion: new FormControl(null),
    estado: new FormControl(false),
    paqueteid: new FormControl(null)
  }, { updateOn: 'submit' });

  getMessage(key: string, details?: any) {
    const messages: Record<string, string> = {
      updated: $localize`:@@servicio.update.success:Servicio was updated successfully.`
    };
    return messages[key];
  }

  ngOnInit() {
    this.currentId = +this.route.snapshot.params['id'];
    this.servicioService.getPaqueteidValues()
        .subscribe({
          next: (data) => this.paqueteidValues = data,
          error: (error) => this.errorHandler.handleServerError(error.error)
        });
    this.servicioService.getServicio(this.currentId!)
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
    const data = new ServicioDTO(this.editForm.value);
    this.servicioService.updateServicio(this.currentId!, data)
        .subscribe({
          next: () => this.router.navigate(['/servicios'], {
            state: {
              msgSuccess: this.getMessage('updated')
            }
          }),
          error: (error) => this.errorHandler.handleServerError(error.error, this.editForm, this.getMessage)
        });
  }

}
