import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { ServiceI } from '../../../shared/models/service.model';
import { ServiceTypeI } from '../../../shared/models/servicetype.model';
import { MessageService } from 'primeng/api';
import { ErrorHandlerService } from '../../../shared/services/error-handler.service';
import { ServicesService } from '../../../shared/services/services.service';
import { ServicetypesService } from '../../../shared/services/servicetypes.service';
import { ActivatedRoute, Params, Router } from '@angular/router';
import { AuthService } from '../../../shared/services/auth.service';

@Component({
  selector: 'app-modify-service',
  templateUrl: './modify-service.component.html',
  styleUrl: './modify-service.component.css'
})
export class ModifyServiceComponent implements OnInit {
  modifyServiceForm!: FormGroup;
  currentService!: ServiceI;
  serviceTypes: ServiceTypeI[] | undefined;

  constructor(private messageService: MessageService,
    private formBuilder: FormBuilder,
    private errorHandlerService: ErrorHandlerService,
    private servicesService: ServicesService,
    private serviceTypesService: ServicetypesService,
    private router: Router,
    private route: ActivatedRoute,
    private authService: AuthService
  ) { }

  ngOnInit(): void {
    // richiediamo i dati del servizio il cui id viene passato come parametro della route
    this.route.params.subscribe((params: Params) => {
      // controlliamo che sia stato passato il parametro nella url
      if(params['idService'] !== undefined) {
        this.servicesService.getService(parseInt(params['idService'])).subscribe({
          next: (service) => {
            this.currentService = service;

            this.serviceTypesService.getServiceTypes().subscribe({
              next: (servTypes) => {
                this.serviceTypes = servTypes;
              },
              error: (err) => this.errorHandlerService.handle(err, undefined, 'modifyServiceToast')
            });
        
            if(this.isAdmin()) {
              this.modifyServiceForm = this.formBuilder.group({
                'name': new FormControl(this.currentService.name, Validators.required),
                'serviceType': new FormControl(this.currentService.idServiceType, Validators.required),
                'validity': new FormControl(this.currentService.validity)
              });
            } else {
              this.modifyServiceForm = this.formBuilder.group({
                'name': new FormControl(this.currentService.name, Validators.required),
                'serviceType': new FormControl(this.currentService.idServiceType, Validators.required),
              });
            }
          },
          error: (err) => this.errorHandlerService.handle(err, undefined, 'modifyServiceToast')
        });
      } else {
        // altrimenti redirigiamo alla home
        this.messageService.add({ 
            key: 'modifyServiceToast',
            severity: 'error',
            summary: 'Errore di Pagina',
            detail: 'Parametro non valido, impossibile caricare la pagina'
        });
        setTimeout(() => this.router.navigate(['home']), 500);
      }
    });
  }

  onSubmit(event: any) {
    event.preventDefault();
    event.stopPropagation();

    if(!this.modifyServiceForm.valid)
      return;

    this.messageService.clear();

    this.currentService.name = this.modifyServiceForm.controls['name'].value;
    this.currentService.idServiceType = parseInt(this.modifyServiceForm.controls['serviceType'].value);
    if(this.isAdmin())
      this.currentService.validity = this.modifyServiceForm.controls['validity'].value

    this.servicesService.updateService(this.currentService).subscribe({
      next: () => {
        this.messageService.add({ key: 'modifyServiceToast',
          severity: 'success',
          summary: 'Successo',
          detail: 'Servizio modificato con successo'
        });

        setTimeout(() => this.router.navigate(['services', 'service', this.currentService.idService]), 1000);
      },
      error: (err) => this.errorHandlerService.handle(err, undefined, 'modifyServiceToast')
    });
  }

  isAdmin(): boolean {
    return this.authService.isAdmin();
  }
}
