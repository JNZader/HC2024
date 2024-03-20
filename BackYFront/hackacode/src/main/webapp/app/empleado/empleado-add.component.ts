import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterLink } from '@angular/router';
import { ReactiveFormsModule, FormControl, FormGroup, Validators } from '@angular/forms';
import { InputRowComponent } from 'app/common/input-row/input-row.component';
import { EmpleadoService } from 'app/empleado/empleado.service';
import { EmpleadoDTO } from 'app/empleado/empleado.model';
import { ErrorHandler } from 'app/common/error-handler.injectable';
import { validDouble } from 'app/common/utils';


@Component({
  selector: 'app-empleado-add',
  standalone: true,
  imports: [CommonModule, RouterLink, ReactiveFormsModule, InputRowComponent],
  templateUrl: './empleado-add.component.html'
})
export class EmpleadoAddComponent {

  empleadoService = inject(EmpleadoService);
  router = inject(Router);
  errorHandler = inject(ErrorHandler);

  addForm = new FormGroup({
    nombre: new FormControl(null, [Validators.maxLength(255)]),
    apellido: new FormControl(null, [Validators.maxLength(255)]),
    direccion: new FormControl(null, [Validators.maxLength(255)]),
    dni: new FormControl(null, [Validators.maxLength(255)]),
    fechaNacimiento: new FormControl(null),
    nacionalidad: new FormControl(null, [Validators.maxLength(255)]),
    celular: new FormControl(null, [Validators.maxLength(255)]),
    email: new FormControl(null, [Validators.maxLength(255)]),
    cargo: new FormControl(null, [Validators.required]),
    sueldo: new FormControl(null, [validDouble]),
    estado: new FormControl(false)
  }, { updateOn: 'submit' });

  getMessage(key: string, details?: any) {
    const messages: Record<string, string> = {
      created: $localize`:@@empleado.create.success:Empleado was created successfully.`
    };
    return messages[key];
  }

  handleSubmit() {
    window.scrollTo(0, 0);
    this.addForm.markAllAsTouched();
    if (!this.addForm.valid) {
      return;
    }
    const data = new EmpleadoDTO(this.addForm.value);
    this.empleadoService.createEmpleado(data)
        .subscribe({
          next: () => this.router.navigate(['/empleados'], {
            state: {
              msgSuccess: this.getMessage('created')
            }
          }),
          error: (error) => this.errorHandler.handleServerError(error.error, this.addForm, this.getMessage)
        });
  }

}
