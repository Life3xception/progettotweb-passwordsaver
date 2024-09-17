import { Component, OnInit } from '@angular/core';
import { DetailedUserI } from '../../shared/models/detailed-user.model';
import { UserTypeI } from '../../shared/models/user-type.model';
import { ErrorHandlerService } from '../../shared/services/error-handler.service';
import { ActivatedRoute, Params, Router } from '@angular/router';
import { MessageService } from 'primeng/api';
import { UsersService } from '../../shared/services/users.service';
import { UsertypesService } from '../../shared/services/usertypes.service';

@Component({
  selector: 'app-users',
  templateUrl: './users.component.html',
  styleUrl: './users.component.css'
})
export class UsersComponent implements OnInit {
  users: DetailedUserI[] | undefined;
  selectedIdUserType: number | undefined;
  selectedIdUser: number | undefined;
  pageTitle: string = 'Lista degli Utenti';
  userTypes: UserTypeI[] | undefined;
  userToDelete: DetailedUserI | undefined;

  constructor(private errorHandlerService: ErrorHandlerService,
    private router: Router,
    private route: ActivatedRoute,
    private messageService: MessageService,
    private usersService: UsersService,
    private userTypesService: UsertypesService,
  ) { }

  ngOnInit(): void {
    this.userTypesService.getUserTypes().subscribe({
      next: (userTypes) => {
        this.userTypes = userTypes;
      },
      error: (err) => this.errorHandlerService.handle(err, undefined, 'usersToast')
    });

    this.route.params.subscribe((params: Params) => {
      this.selectedIdUser = 0;
      this.selectedIdUserType = 0;
      this.pageTitle = 'Lista degli Utenti';

      if(params['specificView'] && params['value']) { // può essere user=id oppure usertype=id
        const view = params['specificView'];
        const value = params['value'];
        if(view === 'usertype') {
          this.selectedIdUserType = parseInt(value);
          this.pageTitle = 'Lista Utenti per Tipo Utente';
        }
        else if(view === 'user') {
          this.selectedIdUser = parseInt(value);
          this.pageTitle = 'Dettaglio Utente';
        }
        else {
          this.messageService.add({ 
              key: 'usersToast',
              severity: 'error',
              summary: 'Errore di Pagina',
              detail: 'Parametro non valido, impossibile caricare la pagina'
          });
          setTimeout(() => this.router.navigate(['home']), 500);
        }
      }

      if(this.selectedIdUser != 0) {
        this.usersService.getDetailedUser(this.selectedIdUser).subscribe({
          next: (user) => {
            this.users = [user];
          },
          error: (err) => this.errorHandlerService.handle(err, undefined, 'usersToast')
        });
      } else if(this.selectedIdUserType != 0) {
        this.usersService.getDetailedUsersByUserType(this.selectedIdUserType).subscribe({
          next: (user) => {
            this.users = user;
          },
          error: (err) => this.errorHandlerService.handle(err, undefined, 'usersToast')
        });
      } else {
        this.usersService.getDetailedUsers().subscribe({
          next: (users) => {
            this.users = users;
          },
          error: (err) => this.errorHandlerService.handle(err, undefined, 'usersToast')
        }); 
      }
    });
  }

  goToNewUser(): void {
    this.router.navigate(['users', 'new-user']);
  }

  showSelectedUserType(idUserType: string): void {
    if(idUserType !== '0')
      this.router.navigate(['users', 'usertype', idUserType]);
    else
      this.router.navigate(['users']);
  }

  modifyUser(user: DetailedUserI): void {
    this.router.navigate(['users', 'modify-user', user.idUser]);
  }

  deleteUser(user: DetailedUserI): void {
    this.userToDelete = user;
  }
  
  confirmDeleteUser(): void {
    if(this.userToDelete !== undefined) {
      this.usersService.deleteUser(this.userToDelete.idUser).subscribe({
        next: () => {
          this.messageService.add({ key: 'usersToast',
            severity: 'success',
            summary: 'Successo',
            detail: 'Utente eliminato con successo'
          });
          
          if(this.selectedIdUser != 0) {
            // eravamo in pagina dettaglio utente, dobbiamo tornare alla lista utenti
            setTimeout(() => window.location.href = 'users', 500);
          } else  {
            // eravamo in pagina user by usertype/ lista utenti, dobbiamo ricaricare la pagina
            setTimeout(() => window.location.reload(), 500);
          }
        },
        error: (err) => this.errorHandlerService.handle(err, undefined, 'usersToast')
      });
    }
  }
}
