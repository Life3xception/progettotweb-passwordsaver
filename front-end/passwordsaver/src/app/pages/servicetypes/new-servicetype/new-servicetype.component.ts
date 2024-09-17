import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { MessageService } from 'primeng/api';
import { ErrorHandlerService } from '../../../shared/services/error-handler.service';
import { ServicetypesService } from '../../../shared/services/servicetypes.service';
import { Router } from '@angular/router';
import { ServiceTypeI } from '../../../shared/models/servicetype.model';

@Component({
  selector: 'app-new-servicetype',
  templateUrl: './new-servicetype.component.html',
  styleUrl: './new-servicetype.component.css'
})
export class NewServicetypeComponent implements OnInit {
  newServiceTypeForm!: FormGroup;

  constructor(private formBuilder: FormBuilder,
    private messageService: MessageService,
    private errorHandlerService: ErrorHandlerService,
    private serviceTypesService: ServicetypesService,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.newServiceTypeForm = this.formBuilder.group({
      'name': new FormControl('', Validators.required),
      'validity': new FormControl(true, Validators.required),
    });
  }

  onSubmit(event: any) {
    event.preventDefault();
    event.stopPropagation();

    if(!this.newServiceTypeForm.valid)
      return;

    this.messageService.clear();

    let servType: ServiceTypeI = {
      idServiceType: 0,
      name: this.newServiceTypeForm.controls['name'].value,
      validity: this.newServiceTypeForm.controls['validity'].value
    };

    this.serviceTypesService.addServiceType(servType).subscribe({
      next: (response) => {
        this.messageService.add({ key: 'newServiceTypeToast',
          severity: 'success',
          summary: 'Successo',
          detail: 'Tipo di Servizio aggiunto con successo!'
        });

        setTimeout(() => this.router.navigate(['servicetypes', 'servicetype', response.idServiceType]), 1000);
      },
      error: (err) => this.errorHandlerService.handle(err, undefined, 'newServiceTypeToast')
    });
  }
}
