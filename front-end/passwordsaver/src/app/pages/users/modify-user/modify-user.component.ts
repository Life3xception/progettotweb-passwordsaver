import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { UserI } from '../../../shared/models/user.model';
import { UserTypeI } from '../../../shared/models/user-type.model';
import { MessageService } from 'primeng/api';
import { ErrorHandlerService } from '../../../shared/services/error-handler.service';
import { UsertypesService } from '../../../shared/services/usertypes.service';
import { UsersService } from '../../../shared/services/users.service';
import { ActivatedRoute, Params, Router } from '@angular/router';
import { environment } from '../../../environment/environment';
import { AuthService } from '../../../shared/services/auth.service';

@Component({
  selector: 'app-modify-user',
  templateUrl: './modify-user.component.html',
  styleUrl: './modify-user.component.css'
})
export class ModifyUserComponent implements OnInit {
  modifyUserForm!: FormGroup;
  currentUser!: UserI;
  userTypes: UserTypeI[] | undefined;

  constructor(private messageService: MessageService,
    private formBuilder: FormBuilder,
    private errorHandlerService: ErrorHandlerService,
    private userTypesService: UsertypesService,
    private usersService: UsersService,
    private router: Router,
    private route: ActivatedRoute,
    private authService: AuthService
  ) { }

  ngOnInit(): void {
    // richiediamo i dati dell'utente il cui id viene passato come parametro della route
    this.route.params.subscribe((params: Params) => {
      // controlliamo che sia stato passato il parametro nella url
      if(params['idUser'] !== undefined) {
        this.usersService.getUser(parseInt(params['idUser'])).subscribe({
          next: (user) => {
            this.currentUser = user;

            if(this.isAdmin()) {
              this.userTypesService.getUserTypes().subscribe({
                next: (userTypes) => {
                  this.userTypes = userTypes;
                },
                error: (err) => this.errorHandlerService.handle(err, undefined, 'modifyUserToast')
              });

              this.modifyUserForm = this.formBuilder.group({
                'username': new FormControl(this.currentUser.username, Validators.required),
                'email': new FormControl(this.currentUser.email, Validators.compose([Validators.required, Validators.pattern(environment.emailRegex)])),
                'userType': new FormControl(this.currentUser.idUserType, Validators.required),
                'validity': new FormControl(this.currentUser.validity),
              });
            } else {
              this.modifyUserForm = this.formBuilder.group({
                'username': new FormControl(this.currentUser.username, Validators.required),
                'email': new FormControl(this.currentUser.email, Validators.compose([Validators.required, Validators.pattern(environment.emailRegex)]))
              });
            }
          },
          error: (err) => this.errorHandlerService.handle(err, undefined, 'modifyUserToast')
        });
      } else {
        // altrimenti redirigiamo alla home
        this.messageService.add({ 
            key: 'modifyUserToast',
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

    if(!this.modifyUserForm.valid)
      return;

    this.messageService.clear();
    
    this.currentUser.username = this.modifyUserForm.controls['username'].value;
    this.currentUser.email = this.modifyUserForm.controls['email'].value;

    if(this.isAdmin()) {
      this.currentUser.idUserType = parseInt(this.modifyUserForm.controls['userType'].value);
      this.currentUser.validity = this.modifyUserForm.controls['validity'].value;
    }

    this.usersService.updateUser(this.currentUser).subscribe({
      next: () => {
        this.messageService.add({ key: 'modifyUserToast',
          severity: 'success',
          summary: 'Successo',
          detail: 'Utente modificato con successo'
        });

        setTimeout(() => this.router.navigate(['users', 'user', this.currentUser.idUser]), 1000);
      },
      error: (err) => this.errorHandlerService.handle(err, undefined, 'modifyUserToast')
    });
  }

  isAdmin(): boolean {
    return this.authService.isAdmin();
  }
}
