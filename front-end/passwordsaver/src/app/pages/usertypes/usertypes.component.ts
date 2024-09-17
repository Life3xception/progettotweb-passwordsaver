import { Component, OnInit } from '@angular/core';
import { UserTypeI } from '../../shared/models/user-type.model';
import { ErrorHandlerService } from '../../shared/services/error-handler.service';
import { ActivatedRoute, Params, Router } from '@angular/router';
import { MessageService } from 'primeng/api';
import { UsertypesService } from '../../shared/services/usertypes.service';

@Component({
  selector: 'app-usertypes',
  templateUrl: './usertypes.component.html',
  styleUrl: './usertypes.component.css'
})
export class UsertypesComponent implements OnInit {
  userTypes: UserTypeI[] | undefined;
  selectedIdUserType: number | undefined;
  pageTitle: string = 'Lista dei Tipi di Utenti';
  userTypeToDelete: UserTypeI | undefined;

  constructor(private errorHandlerService: ErrorHandlerService,
    private router: Router,
    private route: ActivatedRoute,
    private messageService: MessageService,
    private userTypesService: UsertypesService,
  ) { }

  ngOnInit(): void {
    this.route.params.subscribe((params: Params) => {
      this.selectedIdUserType = 0;
      this.pageTitle = 'Lista dei Tipi di Utenti';

      if(params['specificView'] && params['value']) { // può essere solo usertype=id
        const view = params['specificView'];
        const value = params['value'];
        if(view === 'usertype') {
          this.selectedIdUserType = parseInt(value);
          this.pageTitle = 'Dettaglio Tipo di Utente';
        }
        else {
          this.messageService.add({ 
              key: 'userTypeToast',
              severity: 'error',
              summary: 'Errore di Pagina',
              detail: 'Parametro non valido, impossibile caricare la pagina'
          });
          setTimeout(() => this.router.navigate(['home']), 500);
        }
      }

      if(this.selectedIdUserType != 0) {
        this.userTypesService.getUserType(this.selectedIdUserType).subscribe({
          next: (uType) => {
            this.userTypes = [uType];
          },
          error: (err) => this.errorHandlerService.handle(err, undefined, 'userTypeToast')
        });
      } else {
        this.userTypesService.getUserTypes().subscribe({
          next: (userTypes) => {
            this.userTypes = userTypes;
          },
          error: (err) => this.errorHandlerService.handle(err, undefined, 'userTypeToast')
        }); 
      }
    });
  }

  goToNewUserType(): void {
    this.router.navigate(['usertypes', 'new-usertype']);
  }

  modifyUserType(uType: UserTypeI): void {
    this.router.navigate(['usertypes', 'modify-usertype', uType.idUserType]);
  }

  deleteUserType(uType: UserTypeI): void {
    this.userTypeToDelete = uType;
  }
  
  confirmDeleteUserType(): void {
    if(this.userTypeToDelete !== undefined) {
      this.userTypesService.deleteUserType(this.userTypeToDelete.idUserType).subscribe({
        next: () => {
          this.messageService.add({ key: 'userTypesToast',
            severity: 'success',
            summary: 'Successo',
            detail: 'Tipo di Utente eliminato con successo'
          });
          
          if(this.selectedIdUserType != 0) {
            // eravamo in pagina dettaglio tipo di utente, dobbiamo tornare alla lista tipi di utenti
            setTimeout(() => window.location.href = 'usertypes', 500);
          } else  {
            // eravamo in pagina lista tipi di utenti, dobbiamo ricaricare la pagina
            setTimeout(() => window.location.reload(), 500);
          }
        },
        error: (err) => this.errorHandlerService.handle(err, undefined, 'userTypesToast')
      });
    }
  }
}
