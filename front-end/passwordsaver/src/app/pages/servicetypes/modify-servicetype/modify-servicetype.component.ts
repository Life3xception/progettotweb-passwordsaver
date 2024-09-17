import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { ServiceTypeI } from '../../../shared/models/servicetype.model';
import { MessageService } from 'primeng/api';
import { ErrorHandlerService } from '../../../shared/services/error-handler.service';
import { ServicetypesService } from '../../../shared/services/servicetypes.service';
import { ActivatedRoute, Params, Router } from '@angular/router';

@Component({
  selector: 'app-modify-servicetype',
  templateUrl: './modify-servicetype.component.html',
  styleUrl: './modify-servicetype.component.css'
})
export class ModifyServicetypeComponent implements OnInit {
  modifyServiceTypeForm!: FormGroup;
  currentServiceType!: ServiceTypeI;

  constructor(private messageService: MessageService,
    private formBuilder: FormBuilder,
    private errorHandlerService: ErrorHandlerService,
    private serviceTypesService: ServicetypesService,
    private router: Router,
    private route: ActivatedRoute
  ) { }

  ngOnInit(): void {
    // richiediamo i dati del tipo di servizio il cui id viene passato come parametro della route
    this.route.params.subscribe((params: Params) => {
      // controlliamo che sia stato passato il parametro nella url
      if(params['idServiceType'] !== undefined) {
        this.serviceTypesService.getServiceType(parseInt(params['idServiceType'])).subscribe({
          next: (servType) => {
            this.currentServiceType = servType;
        
            this.modifyServiceTypeForm = this.formBuilder.group({
              'name': new FormControl(this.currentServiceType.name, Validators.required),
              'validity': new FormControl(this.currentServiceType.validity, Validators.required),
            });
          },
          error: (err) => this.errorHandlerService.handle(err, undefined, 'modifyServiceTypeToast')
        });
      } else {
        // altrimenti redirigiamo alla home
        this.messageService.add({ 
            key: 'modifyServiceTypeToast',
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

    if(!this.modifyServiceTypeForm.valid)
      return;

    this.messageService.clear();

    this.currentServiceType.name = this.modifyServiceTypeForm.controls['name'].value;
    this.currentServiceType.validity = this.modifyServiceTypeForm.controls['validity'].value;

    this.serviceTypesService.updateServiceType(this.currentServiceType).subscribe({
      next: () => {
        this.messageService.add({ key: 'modifyServiceTypeToast',
          severity: 'success',
          summary: 'Successo',
          detail: 'Tipo di Servizio modificato con successo'
        });

        setTimeout(() => this.router.navigate(['servicetypes', 'servicetype', this.currentServiceType.idServiceType]), 1000);
      },
      error: (err) => this.errorHandlerService.handle(err, undefined, 'modifyServiceTypeToast')
    });
  }
}
