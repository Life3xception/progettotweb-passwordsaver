import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { AuthService } from '../../shared/services/auth.service';
import { MessageService } from 'primeng/api';
import { ErrorHandlerService } from '../../shared/services/error-handler.service';
import { UserI } from '../../shared/models/user.model';
import { TwoControlsMatchingValidatorDirective } from '../../shared/directives/two-controls-matching-validator.directive';
import { environment } from '../../environment/environment';

@Component({
  selector: 'app-signup',
  templateUrl: './signup.component.html',
  styleUrl: './signup.component.css'
})
export class SignupComponent implements OnInit {
  signupForm!: FormGroup;
  
  constructor(private formBuilder: FormBuilder,
    private authService: AuthService,
    private messageService: MessageService,
    private errorHandlerService: ErrorHandlerService
  ) { }

  ngOnInit(): void {
    this.signupForm = this.formBuilder.group({
      'username': new FormControl('', Validators.required),
      'email': new FormControl('', Validators.compose([Validators.required, Validators.pattern(environment.emailRegex)])),
      'password': new FormControl('', Validators.compose([Validators.required, Validators.pattern(environment.pwdRegex)])),
      'confirmPassword': new FormControl('', Validators.compose([Validators.required, Validators.pattern(environment.pwdRegex)]))
    }, {
      // validators permette di passare dei validatori aggiuntivi a livello di formgroup
      // gli passiamo il validator che controlla se due campi corrispondono
      validators: TwoControlsMatchingValidatorDirective.twoControlsMatchingValidator('password', 'confirmPassword', 'Passwords don\'t match.')
    });
  }

  onSubmit(event: any) {
    event.preventDefault();
    event.stopPropagation();

    if(!this.signupForm.valid)
      return;

    this.messageService.clear();

    let user: UserI = {
      idUser: 0,
      username: this.signupForm.controls['username'].value,
      email: this.signupForm.controls['email'].value,
      password: this.signupForm.controls['password'].value,
      idUserType: 0,
      validity: true
    };

    this.authService.signup(user).subscribe({
      next: (response) => {
        this.messageService.add({ key: 'signupToast',
          severity: response.success ? 'success' : 'error',
          summary: response.success ? 'Registrato correttamente' : 'Errore durante la registrazione',
          detail: response.errorMessage
        });

        // uso window.location.href e non router.navigate perché mi serve che venga ricaricata la pagina
        if(response.success) {
          setTimeout(() => window.location.href = '/login', 1000);
        }
      },
      error: (err) => this.errorHandlerService.handle(err, undefined, 'signupToast')
    });
  }

  isPwdValid(pwd: string): boolean {
    return new RegExp(environment.pwdRegex).test(pwd);
  }
}
