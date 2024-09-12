import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { environment } from '../../../environment/environment';
import { Password } from '../../../shared/models/password.model';
import { MessageService } from 'primeng/api';
import { ServicesService } from '../../../shared/services/services.service';
import { ErrorHandlerService } from '../../../shared/services/error-handler.service';
import { Service } from '../../../shared/models/service.model';
import { PasswordsService } from '../../../shared/services/passwords.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-new-password',
  templateUrl: './new-password.component.html',
  styleUrl: './new-password.component.css'
})
export class NewPasswordComponent implements OnInit {
  newPwdForm!: FormGroup;
  services: Service[] | undefined;

  constructor(private formBuilder: FormBuilder,
    private messageService: MessageService,
    private servicesService: ServicesService,
    private errorHandlerService: ErrorHandlerService,
    private passwordsService: PasswordsService,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.servicesService.getServices().subscribe({
      next: (services) => {
        this.services = services;
      },
      error: (err) => this.errorHandlerService.handle(err, undefined, 'newPasswordToast')
    });

    this.newPwdForm = this.formBuilder.group({
      'name': new FormControl('', Validators.required),
      'password': new FormControl('', Validators.required),
      'username': new FormControl(''),
      'email': new FormControl(''),
      'passPhrase': new FormControl(''),
      'isStarred': new FormControl(false),
      'service': new FormControl('0', Validators.required),
    });

    this.newPwdForm.get('email')?.valueChanges.subscribe(val => {
      const emailControl = this.newPwdForm.get('email');

      if (val !== '' && !emailControl?.hasError('pattern')) {
        // If there is a value and the pattern validator is not set
        emailControl?.setValidators([Validators.pattern(environment.emailRegex)]); // set validation only if present
      } else if (val === '' && emailControl?.hasValidator(Validators.pattern(environment.emailRegex))) {
        // If the value is empty and pattern validator is present
        emailControl?.clearValidators(); // remove validation
      }
    
      // Update only if the validators have actually changed
      emailControl?.updateValueAndValidity({ emitEvent: false });
    });
  }

  isEmailValid(email: string): boolean { 
    return new RegExp(environment.emailRegex).test(email);
  }

  onSubmit(event: any) {
    event.preventDefault();
    event.stopPropagation();

    if(!this.newPwdForm.valid)
      return;

    this.messageService.clear();

    let pwd: Password = {
      idPassword: 0,
      name: this.newPwdForm.controls['name'].value,
      password: this.newPwdForm.controls['password'].value,
      username: this.newPwdForm.controls['username'].value,
      email: this.newPwdForm.controls['email'].value,
      passPhrase: this.newPwdForm.controls['passPhrase'].value,
      isStarred: this.newPwdForm.controls['isStarred'].value,
      idUser: 0, // viene settato lato be
      idService: parseInt(this.newPwdForm.controls['service'].value),
      validity: true,
      showed: false
    };

    this.passwordsService.addPassword(pwd).subscribe({
      next: (response) => {
        this.messageService.add({ key: 'newPasswordToast',
          severity: 'success',
          summary: 'Success',
          detail: 'Password added successfully!'
        });

        setTimeout(() => this.router.navigate(['passwords', 'password', response.idPassword]), 1000);
      },
      error: (err) => this.errorHandlerService.handle(err, undefined, 'newPasswordToast')
    });
  }
}
