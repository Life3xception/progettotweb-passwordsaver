import { Component } from '@angular/core';
import { ErrorHandlerService } from '../../shared/services/error-handler.service';
import { ActivatedRoute, Params, Router } from '@angular/router';
import { MessageService } from 'primeng/api';
import { ServicesService } from '../../shared/services/services.service';
import { DetailedService } from '../../shared/models/detailed-service.model';
import { ServiceType } from '../../shared/models/servicetype.model';
import { ServicetypesService } from '../../shared/services/servicetypes.service';

@Component({
  selector: 'app-services',
  templateUrl: './services.component.html',
  styleUrl: './services.component.css'
})
export class ServicesComponent {
  services: DetailedService[] | undefined;
  selectedIdServiceType: number | undefined;
  selectedIdService: number | undefined;
  pageTitle: string = 'Lista dei Servizi';
  serviceTypes: ServiceType[] | undefined;
  serviceToDelete: DetailedService | undefined;

  constructor(private errorHandlerService: ErrorHandlerService,
    private router: Router,
    private route: ActivatedRoute,
    private messageService: MessageService,
    private servicesService: ServicesService,
    private serviceTypesService: ServicetypesService,
  ) { }

  ngOnInit(): void {
    this.serviceTypesService.getServiceTypes().subscribe({
      next: (serviceTypes) => {
        this.serviceTypes = serviceTypes;
      },
      error: (err) => this.errorHandlerService.handle(err, undefined, 'servicesToast')
    });

    this.route.params.subscribe((params: Params) => {
      this.selectedIdService = 0;
      this.selectedIdServiceType = 0;
      this.pageTitle = 'Lista dei Servizi';

      if(params['specificView'] && params['value']) { // può essere service=id oppure servicetype=id
        const view = params['specificView'];
        const value = params['value'];
        if(view === 'servicetype') {
          this.selectedIdServiceType = parseInt(value);
          this.pageTitle = 'Lista Servizi per Tipo Servizio';
        }
        else if(view === 'service') {
          this.selectedIdService = parseInt(value);
          this.pageTitle = 'Dettaglio Servizio';
        }
        else {
          this.messageService.add({ 
              key: 'servicesToast',
              severity: 'error',
              summary: 'Errore di Pagina',
              detail: 'Parametro non valido, impossibile caricare la pagina'
          });
          setTimeout(() => this.router.navigate(['home']), 500);
        }
      }

      if(this.selectedIdService != 0) {
        this.servicesService.getDetailedService(this.selectedIdService).subscribe({
          next: (serv) => {
            this.services = [serv];
          },
          error: (err) => this.errorHandlerService.handle(err, undefined, 'servicesToast')
        });
      } else if(this.selectedIdServiceType != 0) {
        this.servicesService.getDetailedServicesByServiceType(this.selectedIdServiceType).subscribe({
          next: (services) => {
            this.services = services;
          },
          error: (err) => this.errorHandlerService.handle(err, undefined, 'servicesToast')
        });
      } else {
        this.servicesService.getDetailedServices().subscribe({
          next: (services) => {
            this.services = services;
          },
          error: (err) => this.errorHandlerService.handle(err, undefined, 'servicesToast')
        }); 
      }
    });
  }

  goToNewService(): void {
    this.router.navigate(['services', 'new-service']);
  }

  showSelectedServiceType(idServiceType: string): void {
    if(idServiceType !== '0')
      this.router.navigate(['services', 'servicetype', idServiceType]);
    else
      this.router.navigate(['services']);
  }

  modifyService(serv: DetailedService): void {
    this.router.navigate(['services', 'modify-service', serv.idService]);
  }

  deleteService(serv: DetailedService): void {
    this.serviceToDelete = serv;
  }
  
  confirmDeleteService(): void {
    if(this.serviceToDelete !== undefined) {
      this.servicesService.deleteService(this.serviceToDelete.idService).subscribe({
        next: () => {
          this.messageService.add({ key: 'servicesToast',
            severity: 'success',
            summary: 'Successo',
            detail: 'Servizio eliminato con successo'
          });
          
          if(this.selectedIdService != 0) {
            // eravamo in pagina dettaglio servizio, dobbiamo tornare alla lista servizi
            setTimeout(() => window.location.href = 'services', 500);
          } else  {
            // eravamo in pagina service by servicetype/ lista servizi, dobbiamo ricaricare la pagina
            setTimeout(() => window.location.reload(), 500);
          }
        },
        error: (err) => this.errorHandlerService.handle(err, undefined, 'servicesToast')
      });
    }
  }
}
