import { Component, Input, OnInit } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { MessageService } from 'primeng/api';
import { UsersService } from '../../../shared/services/users.service';
import { ErrorHandlerService } from '../../../shared/services/error-handler.service';
import { UsertypesService } from '../../../shared/services/usertypes.service';
import { Router } from '@angular/router';
import { environment } from '../../../environment/environment';
import { UserI } from '../../../shared/models/user.model';
import { UserTypeI } from '../../../shared/models/user-type.model';
import { AuthService } from '../../../shared/services/auth.service';

@Component({
  selector: 'app-user-form',
  templateUrl: './user-form.component.html',
  styleUrl: './user-form.component.css'
})
export class UserFormComponent implements OnInit {
  userForm!: FormGroup;
  userTypes: UserTypeI[] | undefined;
  currentUser!: UserI;
  @Input() page!: string;
  @Input() idUser!: number;
  @Input() toastKey!: string;
  @Input() title!: string;
  @Input() submitButton!: string;

  constructor(private formBuilder: FormBuilder,
    private messageService: MessageService,
    private usersService: UsersService,
    private errorHandlerService: ErrorHandlerService,
    private userTypesService: UsertypesService,
    private router: Router,
    private authService: AuthService
  ) { }

  ngOnInit(): void {
    if(this.page === 'new-user') {
      this.userTypesService.getUserTypes().subscribe({
        next: (userTypes) => {
          this.userTypes = userTypes;
        },
        error: (err) => this.errorHandlerService.handle(err, undefined, this.toastKey)
      });

      this.userForm = this.formBuilder.group({
        'username': new FormControl('', Validators.required),
        'email': new FormControl('', Validators.compose([Validators.required, Validators.pattern(environment.emailRegex)])),
        'password': new FormControl('', Validators.compose([Validators.required, Validators.pattern(environment.pwdRegex)])),
        'userType': new FormControl('', Validators.required),
        'validity': new FormControl(true),
      });
    } else if(this.page === 'modify-user') {
      this.usersService.getUser(this.idUser).subscribe({
        next: (user) => {
          this.currentUser = user;

          if(this.isAdmin()) {
            this.userTypesService.getUserTypes().subscribe({
              next: (userTypes) => {
                this.userTypes = userTypes;
              },
              error: (err) => this.errorHandlerService.handle(err, undefined, this.toastKey)
            });

            this.userForm = this.formBuilder.group({
              'username': new FormControl(this.currentUser.username, Validators.required),
              'email': new FormControl(this.currentUser.email, Validators.compose([Validators.required, Validators.pattern(environment.emailRegex)])),
              'userType': new FormControl(this.currentUser.idUserType, Validators.required),
              'validity': new FormControl(this.currentUser.validity),
            });
          } else {
            this.userForm = this.formBuilder.group({
              'username': new FormControl(this.currentUser.username, Validators.required),
              'email': new FormControl(this.currentUser.email, Validators.compose([Validators.required, Validators.pattern(environment.emailRegex)]))
            });
          }
        },
        error: (err) => this.errorHandlerService.handle(err, undefined, this.toastKey)
      });
    }
  }

  onSubmit(event: any) {
    event.preventDefault();
    event.stopPropagation();

    if(!this.userForm.valid)
      return;

    this.messageService.clear();

    if(this.page === 'new-user') {
      this.addUser();
    } else if(this.page === 'modify-user') {
      this.updateUser();
    }
  }

  addUser(): void {
    let user: UserI = {
      idUser: 0,
      username: this.userForm.controls['username'].value,
      email: this.userForm.controls['email'].value,
      password: this.userForm.controls['password'].value,
      idUserType: parseInt(this.userForm.controls['userType'].value),
      validity: this.userForm.controls['validity'].value
    };

    this.usersService.addUser(user).subscribe({
      next: (response) => {
        this.messageService.add({ key: this.toastKey,
          severity: 'success',
          summary: 'Successo',
          detail: 'Utente aggiunto con successo!'
        });

        setTimeout(() => this.router.navigate(['users', 'user', response.idUser]), 1000);
      },
      error: (err) => this.errorHandlerService.handle(err, undefined, this.toastKey)
    });
  }

  updateUser(): void {
    this.currentUser.username = this.userForm.controls['username'].value;
    this.currentUser.email = this.userForm.controls['email'].value;

    if(this.isAdmin()) {
      this.currentUser.idUserType = parseInt(this.userForm.controls['userType'].value);
      this.currentUser.validity = this.userForm.controls['validity'].value;
    }

    this.usersService.updateUser(this.currentUser).subscribe({
      next: () => {
        this.messageService.add({ key: this.toastKey,
          severity: 'success',
          summary: 'Successo',
          detail: 'Utente modificato con successo'
        });

        setTimeout(() => this.router.navigate(['users', 'user', this.currentUser.idUser]), 1000);
      },
      error: (err) => this.errorHandlerService.handle(err, undefined, this.toastKey)
    });
  }

  isAdmin(): boolean {
    return this.authService.isAdmin();
  }
}
