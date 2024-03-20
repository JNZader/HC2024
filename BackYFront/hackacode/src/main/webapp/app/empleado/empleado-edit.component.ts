import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { ReactiveFormsModule, FormControl, FormGroup, Validators } from '@angular/forms';
import { InputRowComponent } from 'app/common/input-row/input-row.component';
import { EmpleadoService } from 'app/empleado/empleado.service';
import { EmpleadoDTO } from 'app/empleado/empleado.model';
import { ErrorHandler } from 'app/common/error-handler.injectable';
import { updateForm, validDouble } from 'app/common/utils';


@Component({
  selector: 'app-empleado-edit',
  standalone: true,
  imports: [CommonModule, RouterLink, ReactiveFormsModule, InputRowComponent],
  templateUrl: './empleado-edit.component.html'
})
export class EmpleadoEditComponent implements OnInit {

  empleadoService = inject(EmpleadoService);
  route = inject(ActivatedRoute);
  router = inject(Router);
  errorHandler = inject(ErrorHandler);

  currentId?: number;

  editForm = new FormGroup({
    id: new FormControl({ value: null, disabled: true }),
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
      updated: $localize`:@@empleado.update.success:Empleado was updated successfully.`
    };
    return messages[key];
  }

  ngOnInit() {
    this.currentId = +this.route.snapshot.params['id'];
    this.empleadoService.getEmpleado(this.currentId!)
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
    const data = new EmpleadoDTO(this.editForm.value);
    this.empleadoService.updateEmpleado(this.currentId!, data)
        .subscribe({
          next: () => this.router.navigate(['/empleados'], {
            state: {
              msgSuccess: this.getMessage('updated')
            }
          }),
          error: (error) => this.errorHandler.handleServerError(error.error, this.editForm, this.getMessage)
        });
  }

}
