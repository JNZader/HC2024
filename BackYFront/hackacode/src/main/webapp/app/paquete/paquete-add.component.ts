import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterLink } from '@angular/router';
import { ReactiveFormsModule, FormControl, FormGroup, Validators } from '@angular/forms';
import { InputRowComponent } from 'app/common/input-row/input-row.component';
import { PaqueteService } from 'app/paquete/paquete.service';
import { PaqueteDTO } from 'app/paquete/paquete.model';
import { ErrorHandler } from 'app/common/error-handler.injectable';
import { validDouble } from 'app/common/utils';


@Component({
  selector: 'app-paquete-add',
  standalone: true,
  imports: [CommonModule, RouterLink, ReactiveFormsModule, InputRowComponent],
  templateUrl: './paquete-add.component.html'
})
export class PaqueteAddComponent {

  paqueteService = inject(PaqueteService);
  router = inject(Router);
  errorHandler = inject(ErrorHandler);

  addForm = new FormGroup({
    tipo: new FormControl(null, [Validators.required]),
    nombre: new FormControl(null, [Validators.maxLength(255)]),
    descripcionBreve: new FormControl(null, [Validators.maxLength(255)]),
    precioVenta: new FormControl(null, [Validators.required, validDouble]),
    duracion: new FormControl(null),
    estado: new FormControl(false)
  }, { updateOn: 'submit' });

  getMessage(key: string, details?: any) {
    const messages: Record<string, string> = {
      created: $localize`:@@paquete.create.success:Paquete was created successfully.`
    };
    return messages[key];
  }

  handleSubmit() {
    window.scrollTo(0, 0);
    this.addForm.markAllAsTouched();
    if (!this.addForm.valid) {
      return;
    }
    const data = new PaqueteDTO(this.addForm.value);
    this.paqueteService.createPaquete(data)
        .subscribe({
          next: () => this.router.navigate(['/paquetes'], {
            state: {
              msgSuccess: this.getMessage('created')
            }
          }),
          error: (error) => this.errorHandler.handleServerError(error.error, this.addForm, this.getMessage)
        });
  }

}
