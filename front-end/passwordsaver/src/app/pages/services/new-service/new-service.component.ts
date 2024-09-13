import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { Service } from '../../../shared/models/service.model';
import { ServiceType } from '../../../shared/models/servicetype.model';
import { MessageService } from 'primeng/api';
import { ServicesService } from '../../../shared/services/services.service';
import { ErrorHandlerService } from '../../../shared/services/error-handler.service';
import { ServicetypesService } from '../../../shared/services/servicetypes.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-new-service',
  templateUrl: './new-service.component.html',
  styleUrl: './new-service.component.css'
})
export class NewServiceComponent implements OnInit {
  newServiceForm!: FormGroup;
  servicesTypes: ServiceType[] | undefined;

  constructor(private formBuilder: FormBuilder,
    private messageService: MessageService,
    private servicesService: ServicesService,
    private errorHandlerService: ErrorHandlerService,
    private serviceTypesService: ServicetypesService,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.serviceTypesService.getServiceTypes().subscribe({
      next: (servTypes) => {
        this.servicesTypes = servTypes;
      },
      error: (err) => this.errorHandlerService.handle(err, undefined, 'newServiceToast')
    });

    this.newServiceForm = this.formBuilder.group({
      'name': new FormControl('', Validators.required),
      'serviceType': new FormControl('0', Validators.required),
    });
  }

  onSubmit(event: any) {
    event.preventDefault();
    event.stopPropagation();

    if(!this.newServiceForm.valid)
      return;

    this.messageService.clear();

    let service: Service = {
      idService: 0,
      name: this.newServiceForm.controls['name'].value,
      idUser: 0, // viene settato lato be
      idServiceType: parseInt(this.newServiceForm.controls['serviceType'].value),
      validity: true
    };

    this.servicesService.addService(service).subscribe({
      next: (response) => {
        this.messageService.add({ key: 'newServiceToast',
          severity: 'success',
          summary: 'Successo',
          detail: 'Servizio aggiunto con successo!'
        });

        setTimeout(() => this.router.navigate(['services', 'service', response.idService]), 1000);
      },
      error: (err) => this.errorHandlerService.handle(err, undefined, 'newServiceToast')
    });
  }
}
