import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { ReactiveFormsModule, FormControl, FormGroup, Validators } from '@angular/forms';
import { InputRowComponent } from 'app/common/input-row/input-row.component';
import { PaqueteService } from 'app/paquete/paquete.service';
import { PaqueteDTO } from 'app/paquete/paquete.model';
import { ErrorHandler } from 'app/common/error-handler.injectable';
import { updateForm, validDouble } from 'app/common/utils';


@Component({
  selector: 'app-paquete-edit',
  standalone: true,
  imports: [CommonModule, RouterLink, ReactiveFormsModule, InputRowComponent],
  templateUrl: './paquete-edit.component.html'
})
export class PaqueteEditComponent implements OnInit {

  paqueteService = inject(PaqueteService);
  route = inject(ActivatedRoute);
  router = inject(Router);
  errorHandler = inject(ErrorHandler);

  currentId?: number;

  editForm = new FormGroup({
    id: new FormControl({ value: null, disabled: true }),
    tipo: new FormControl(null, [Validators.required]),
    nombre: new FormControl(null, [Validators.maxLength(255)]),
    descripcionBreve: new FormControl(null, [Validators.maxLength(255)]),
    precioVenta: new FormControl(null, [Validators.required, validDouble]),
    duracion: new FormControl(null),
    estado: new FormControl(false)
  }, { updateOn: 'submit' });

  getMessage(key: string, details?: any) {
    const messages: Record<string, string> = {
      updated: $localize`:@@paquete.update.success:Paquete was updated successfully.`
    };
    return messages[key];
  }

  ngOnInit() {
    this.currentId = +this.route.snapshot.params['id'];
    this.paqueteService.getPaquete(this.currentId!)
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
    const data = new PaqueteDTO(this.editForm.value);
    this.paqueteService.updatePaquete(this.currentId!, data)
        .subscribe({
          next: () => this.router.navigate(['/paquetes'], {
            state: {
              msgSuccess: this.getMessage('updated')
            }
          }),
          error: (error) => this.errorHandler.handleServerError(error.error, this.editForm, this.getMessage)
        });
  }

}
