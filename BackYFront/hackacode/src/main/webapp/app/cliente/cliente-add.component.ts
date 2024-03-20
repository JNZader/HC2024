import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterLink } from '@angular/router';
import { ReactiveFormsModule, FormControl, FormGroup, Validators } from '@angular/forms';
import { InputRowComponent } from 'app/common/input-row/input-row.component';
import { ClienteService } from 'app/cliente/cliente.service';
import { ClienteDTO } from 'app/cliente/cliente.model';
import { ErrorHandler } from 'app/common/error-handler.injectable';


@Component({
  selector: 'app-cliente-add',
  standalone: true,
  imports: [CommonModule, RouterLink, ReactiveFormsModule, InputRowComponent],
  templateUrl: './cliente-add.component.html'
})
export class ClienteAddComponent {

  clienteService = inject(ClienteService);
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
    estado: new FormControl(false)
  }, { updateOn: 'submit' });

  getMessage(key: string, details?: any) {
    const messages: Record<string, string> = {
      created: $localize`:@@cliente.create.success:Cliente was created successfully.`
    };
    return messages[key];
  }

  handleSubmit() {
    window.scrollTo(0, 0);
    this.addForm.markAllAsTouched();
    if (!this.addForm.valid) {
      return;
    }
    const data = new ClienteDTO(this.addForm.value);
    this.clienteService.createCliente(data)
        .subscribe({
          next: () => this.router.navigate(['/clientes'], {
            state: {
              msgSuccess: this.getMessage('created')
            }
          }),
          error: (error) => this.errorHandler.handleServerError(error.error, this.addForm, this.getMessage)
        });
  }

}
