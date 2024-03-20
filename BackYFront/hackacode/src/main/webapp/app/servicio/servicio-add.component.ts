import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterLink } from '@angular/router';
import { ReactiveFormsModule, FormControl, FormGroup, Validators } from '@angular/forms';
import { InputRowComponent } from 'app/common/input-row/input-row.component';
import { ServicioService } from 'app/servicio/servicio.service';
import { ServicioDTO } from 'app/servicio/servicio.model';
import { ErrorHandler } from 'app/common/error-handler.injectable';


@Component({
  selector: 'app-servicio-add',
  standalone: true,
  imports: [CommonModule, RouterLink, ReactiveFormsModule, InputRowComponent],
  templateUrl: './servicio-add.component.html'
})
export class ServicioAddComponent implements OnInit {

  servicioService = inject(ServicioService);
  router = inject(Router);
  errorHandler = inject(ErrorHandler);

  paqueteidValues?: Map<number,string>;

  addForm = new FormGroup({
    nombre: new FormControl(null, [Validators.maxLength(255)]),
    descripcion: new FormControl(null, [Validators.maxLength(255)]),
    fecha: new FormControl(null),
    duracion: new FormControl(null),
    estado: new FormControl(false),
    paqueteid: new FormControl(null)
  }, { updateOn: 'submit' });

  getMessage(key: string, details?: any) {
    const messages: Record<string, string> = {
      created: $localize`:@@servicio.create.success:Servicio was created successfully.`
    };
    return messages[key];
  }

  ngOnInit() {
    this.servicioService.getPaqueteidValues()
        .subscribe({
          next: (data) => this.paqueteidValues = data,
          error: (error) => this.errorHandler.handleServerError(error.error)
        });
  }

  handleSubmit() {
    window.scrollTo(0, 0);
    this.addForm.markAllAsTouched();
    if (!this.addForm.valid) {
      return;
    }
    const data = new ServicioDTO(this.addForm.value);
    this.servicioService.createServicio(data)
        .subscribe({
          next: () => this.router.navigate(['/servicios'], {
            state: {
              msgSuccess: this.getMessage('created')
            }
          }),
          error: (error) => this.errorHandler.handleServerError(error.error, this.addForm, this.getMessage)
        });
  }

}
