import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { UserTypeI } from '../../../shared/models/user-type.model';
import { MessageService } from 'primeng/api';
import { UsersService } from '../../../shared/services/users.service';
import { ErrorHandlerService } from '../../../shared/services/error-handler.service';
import { UsertypesService } from '../../../shared/services/usertypes.service';
import { Router } from '@angular/router';
import { environment } from '../../../environment/environment';
import { UserI } from '../../../shared/models/user.model';

@Component({
  selector: 'app-new-user',
  templateUrl: './new-user.component.html',
  styleUrl: './new-user.component.css'
})
export class NewUserComponent implements OnInit {
  newUserForm!: FormGroup;
  userTypes: UserTypeI[] | undefined;

  constructor(private formBuilder: FormBuilder,
    private messageService: MessageService,
    private usersService: UsersService,
    private errorHandlerService: ErrorHandlerService,
    private userTypesService: UsertypesService,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.userTypesService.getUserTypes().subscribe({
      next: (userTypes) => {
        this.userTypes = userTypes;
      },
      error: (err) => this.errorHandlerService.handle(err, undefined, 'newUserToast')
    });

    this.newUserForm = this.formBuilder.group({
      'username': new FormControl('', Validators.required),
      'email': new FormControl('', Validators.compose([Validators.required, Validators.pattern(environment.emailRegex)])),
      'password': new FormControl('', Validators.compose([Validators.required, Validators.pattern(environment.pwdRegex)])),
      'userType': new FormControl('', Validators.required),
      'validity': new FormControl(true),
    });
  }

  onSubmit(event: any) {
    event.preventDefault();
    event.stopPropagation();

    if(!this.newUserForm.valid)
      return;

    this.messageService.clear();

    let user: UserI = {
      idUser: 0,
      username: this.newUserForm.controls['username'].value,
      email: this.newUserForm.controls['email'].value,
      password: this.newUserForm.controls['password'].value,
      idUserType: parseInt(this.newUserForm.controls['userType'].value),
      validity: this.newUserForm.controls['validity'].value
    };

    this.usersService.addUser(user).subscribe({
      next: (response) => {
        this.messageService.add({ key: 'newUserToast',
          severity: 'success',
          summary: 'Successo',
          detail: 'Utente aggiunto con successo!'
        });

        setTimeout(() => this.router.navigate(['users', 'user', response.idUser]), 1000);
      },
      error: (err) => this.errorHandlerService.handle(err, undefined, 'newUserToast')
    });
  }
}
