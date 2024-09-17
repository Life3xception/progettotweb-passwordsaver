import { Component, OnInit } from '@angular/core';
import { ServiceTypeI } from '../../shared/models/servicetype.model';
import { ErrorHandlerService } from '../../shared/services/error-handler.service';
import { ActivatedRoute, Params, Router } from '@angular/router';
import { MessageService } from 'primeng/api';
import { ServicetypesService } from '../../shared/services/servicetypes.service';

@Component({
  selector: 'app-servicetypes',
  templateUrl: './servicetypes.component.html',
  styleUrl: './servicetypes.component.css'
})
export class ServicetypesComponent implements OnInit {
  serviceTypes: ServiceTypeI[] | undefined;
  selectedIdServiceType: number | undefined;
  pageTitle: string = 'Lista dei Tipi di Servizi';
  serviceTypeToDelete: ServiceTypeI | undefined;

  constructor(private errorHandlerService: ErrorHandlerService,
    private router: Router,
    private route: ActivatedRoute,
    private messageService: MessageService,
    private serviceTypesService: ServicetypesService,
  ) { }

  ngOnInit(): void {
    this.route.params.subscribe((params: Params) => {
      this.selectedIdServiceType = 0;
      this.pageTitle = 'Lista dei Tipi di Servizi';

      if(params['specificView'] && params['value']) { // può essere solo servicetype=id
        const view = params['specificView'];
        const value = params['value'];
        if(view === 'servicetype') {
          this.selectedIdServiceType = parseInt(value);
          this.pageTitle = 'Dettaglio Tipo di Servizio';
        }
        else {
          this.messageService.add({ 
              key: 'serviceTypeToast',
              severity: 'error',
              summary: 'Errore di Pagina',
              detail: 'Parametro non valido, impossibile caricare la pagina'
          });
          setTimeout(() => this.router.navigate(['home']), 500);
        }
      }

      if(this.selectedIdServiceType != 0) {
        this.serviceTypesService.getServiceType(this.selectedIdServiceType).subscribe({
          next: (serv) => {
            this.serviceTypes = [serv];
          },
          error: (err) => this.errorHandlerService.handle(err, undefined, 'serviceTypeToast')
        });
      } else {
        this.serviceTypesService.getServiceTypes().subscribe({
          next: (services) => {
            this.serviceTypes = services;
          },
          error: (err) => this.errorHandlerService.handle(err, undefined, 'serviceTypeToast')
        }); 
      }
    });
  }

  goToNewServiceType(): void {
    this.router.navigate(['servicetypes', 'new-servicetype']);
  }

  modifyServiceType(servType: ServiceTypeI): void {
    this.router.navigate(['servicetypes', 'modify-servicetype', servType.idServiceType]);
  }

  deleteServiceType(servType: ServiceTypeI): void {
    this.serviceTypeToDelete = servType;
  }
  
  confirmDeleteServiceType(): void {
    if(this.serviceTypeToDelete !== undefined) {
      this.serviceTypesService.deleteServiceType(this.serviceTypeToDelete.idServiceType).subscribe({
        next: () => {
          this.messageService.add({ key: 'serviceTypesToast',
            severity: 'success',
            summary: 'Successo',
            detail: 'Tipo di Servizio eliminato con successo'
          });
          
          if(this.selectedIdServiceType != 0) {
            // eravamo in pagina dettaglio tipo di servizio, dobbiamo tornare alla lista tipi di servizi
            setTimeout(() => window.location.href = 'servicetypes', 500);
          } else  {
            // eravamo in pagina lista tipi di servizi, dobbiamo ricaricare la pagina
            setTimeout(() => window.location.reload(), 500);
          }
        },
        error: (err) => this.errorHandlerService.handle(err, undefined, 'serviceTypesToast')
      });
    }
  }
}
