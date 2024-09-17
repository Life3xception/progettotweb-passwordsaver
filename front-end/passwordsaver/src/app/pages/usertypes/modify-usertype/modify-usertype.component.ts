import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { UserTypeI } from '../../../shared/models/user-type.model';
import { MessageService } from 'primeng/api';
import { ErrorHandlerService } from '../../../shared/services/error-handler.service';
import { UsertypesService } from '../../../shared/services/usertypes.service';
import { ActivatedRoute, Params, Router } from '@angular/router';

@Component({
  selector: 'app-modify-usertype',
  templateUrl: './modify-usertype.component.html',
  styleUrl: './modify-usertype.component.css'
})
export class ModifyUsertypeComponent implements OnInit {
  modifyUserTypeForm!: FormGroup;
  currentUserType!: UserTypeI;

  constructor(private messageService: MessageService,
    private formBuilder: FormBuilder,
    private errorHandlerService: ErrorHandlerService,
    private userTypesService: UsertypesService,
    private router: Router,
    private route: ActivatedRoute
  ) { }

  ngOnInit(): void {
    // richiediamo i dati del tipo di utente il cui id viene passato come parametro della route
    this.route.params.subscribe((params: Params) => {
      // controlliamo che sia stato passato il parametro nella url
      if(params['idUserType'] !== undefined) {
        this.userTypesService.getUserType(parseInt(params['idUserType'])).subscribe({
          next: (uType) => {
            this.currentUserType = uType;
        
            this.modifyUserTypeForm = this.formBuilder.group({
              'name': new FormControl(this.currentUserType.name, Validators.required),
              'validity': new FormControl(this.currentUserType.validity, Validators.required),
            });
          },
          error: (err) => this.errorHandlerService.handle(err, undefined, 'modifyUserTypeToast')
        });
      } else {
        // altrimenti redirigiamo alla home
        this.messageService.add({ 
            key: 'modifyUserTypeToast',
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

    if(!this.modifyUserTypeForm.valid)
      return;

    this.messageService.clear();

    this.currentUserType.name = this.modifyUserTypeForm.controls['name'].value;
    this.currentUserType.validity = this.modifyUserTypeForm.controls['validity'].value;

    this.userTypesService.updateUserType(this.currentUserType).subscribe({
      next: () => {
        this.messageService.add({ key: 'modifyUserTypeToast',
          severity: 'success',
          summary: 'Successo',
          detail: 'Tipo di Utente modificato con successo'
        });

        setTimeout(() => this.router.navigate(['usertypes', 'usertype', this.currentUserType.idUserType]), 1000);
      },
      error: (err) => this.errorHandlerService.handle(err, undefined, 'modifyUserTypeToast')
    });
  }
}
