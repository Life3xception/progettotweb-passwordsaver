import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { ServiceI } from '../../../shared/models/service.model';
import { ServiceTypeI } from '../../../shared/models/servicetype.model';
import { MessageService } from 'primeng/api';
import { ServicesService } from '../../../shared/services/services.service';
import { ErrorHandlerService } from '../../../shared/services/error-handler.service';
import { ServicetypesService } from '../../../shared/services/servicetypes.service';
import { Router } from '@angular/router';
import { AuthService } from '../../../shared/services/auth.service';

@Component({
  selector: 'app-new-service',
  templateUrl: './new-service.component.html',
  styleUrl: './new-service.component.css'
})
export class NewServiceComponent implements OnInit {
  newServiceForm!: FormGroup;
  servicesTypes: ServiceTypeI[] | undefined;

  constructor(private formBuilder: FormBuilder,
    private messageService: MessageService,
    private servicesService: ServicesService,
    private errorHandlerService: ErrorHandlerService,
    private serviceTypesService: ServicetypesService,
    private router: Router,
    private authService: AuthService
  ) { }

  ngOnInit(): void {
    this.serviceTypesService.getServiceTypes().subscribe({
      next: (servTypes) => {
        this.servicesTypes = servTypes;
      },
      error: (err) => this.errorHandlerService.handle(err, undefined, 'newServiceToast')
    });

    if(this.isAdmin()) {
      this.newServiceForm = this.formBuilder.group({
        'name': new FormControl('', Validators.required),
        'serviceType': new FormControl('', Validators.required),
        'validity': new FormControl(true)
      });
    } else {
      this.newServiceForm = this.formBuilder.group({
        'name': new FormControl('', Validators.required),
        'serviceType': new FormControl('', Validators.required),
      });
    }
  }

  onSubmit(event: any) {
    event.preventDefault();
    event.stopPropagation();

    if(!this.newServiceForm.valid)
      return;

    this.messageService.clear();

    let service: ServiceI = {
      idService: 0,
      name: this.newServiceForm.controls['name'].value,
      idUser: 0, // viene settato lato be
      idServiceType: parseInt(this.newServiceForm.controls['serviceType'].value),
      validity: this.isAdmin() ? this.newServiceForm.controls['validity'].value : true
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

  isAdmin(): boolean {
    return this.authService.isAdmin();
  }
}
