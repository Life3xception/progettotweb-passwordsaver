import { Component, OnInit } from '@angular/core';
import { ServiceI } from '../../../shared/models/service.model';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { MessageService } from 'primeng/api';
import { ErrorHandlerService } from '../../../shared/services/error-handler.service';
import { PasswordsService } from '../../../shared/services/passwords.service';
import { ServicesService } from '../../../shared/services/services.service';
import { ActivatedRoute, Params, Router } from '@angular/router';
import { PasswordI } from '../../../shared/models/password.model';
import { environment } from '../../../environment/environment';

@Component({
  selector: 'app-modify-password',
  templateUrl: './modify-password.component.html',
  styleUrl: './modify-password.component.css'
})
export class ModifyPasswordComponent implements OnInit {
  modifyPwdForm!: FormGroup;
  currentPwd!: PasswordI;
  services: ServiceI[] | undefined;

  constructor(private messageService: MessageService,
    private formBuilder: FormBuilder,
    private errorHandlerService: ErrorHandlerService,
    private servicesService: ServicesService,
    private passwordsService: PasswordsService,
    private router: Router,
    private route: ActivatedRoute
  ) { }

  ngOnInit(): void {
    // richiediamo i dati della password il cui id viene passato come parametro della route
    this.route.params.subscribe((params: Params) => {
      // controlliamo che sia stato passato il parametro nella url
      if(params['idPassword'] !== undefined) {
        this.passwordsService.getDecodedPassword(parseInt(params['idPassword'])).subscribe({
          next: (pwd) => {
            this.passwordsService.decodePassword(pwd);
            this.currentPwd = pwd;

            this.servicesService.getServices().subscribe({
              next: (services) => {
                this.services = services;
              },
              error: (err) => this.errorHandlerService.handle(err, undefined, 'modifyPasswordToast')
            });
        
            this.modifyPwdForm = this.formBuilder.group({
              'name': new FormControl(this.currentPwd.name, Validators.required),
              'password': new FormControl(this.currentPwd.password, Validators.required),
              'username': new FormControl(this.currentPwd.username),
              'email': new FormControl(this.currentPwd.email),
              'passPhrase': new FormControl(this.currentPwd.passPhrase),
              'isStarred': new FormControl(this.currentPwd.isStarred),
              'service': new FormControl(this.currentPwd.idService, Validators.required),
            });
        
            this.modifyPwdForm.get('email')?.valueChanges.subscribe(val => {
              const emailControl = this.modifyPwdForm.get('email');
        
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
          },
          error: (err) => this.errorHandlerService.handle(err, undefined, 'modifyPasswordToast')
        });
      } else {
        // altrimenti redirigiamo alla home
        this.messageService.add({ 
            key: 'modifyPasswordToast',
            severity: 'error',
            summary: 'Errore di Pagina',
            detail: 'Parametro non valido, impossibile caricare la pagina'
        });
        setTimeout(() => this.router.navigate(['home']), 500);
      }
    });
  }

  isEmailValid(email: string): boolean { 
    return new RegExp(environment.emailRegex).test(email);
  }

  onSubmit(event: any) {
    event.preventDefault();
    event.stopPropagation();

    if(!this.modifyPwdForm.valid)
      return;

    this.messageService.clear();

    this.currentPwd.name = this.modifyPwdForm.controls['name'].value;
    this.currentPwd.password = this.modifyPwdForm.controls['password'].value;
    this.currentPwd.username = this.modifyPwdForm.controls['username'].value;
    this.currentPwd.email = this.modifyPwdForm.controls['email'].value;
    this.currentPwd.passPhrase = this.modifyPwdForm.controls['passPhrase'].value;
    this.currentPwd.isStarred = this.modifyPwdForm.controls['isStarred'].value;
    this.currentPwd.idService = parseInt(this.modifyPwdForm.controls['service'].value);

    this.passwordsService.updatePassword(this.currentPwd).subscribe({
      next: () => {
        this.messageService.add({ key: 'modifyPasswordToast',
          severity: 'success',
          summary: 'Successo',
          detail: 'Password modificata con successo'
        });

        setTimeout(() => this.router.navigate(['passwords', 'password', this.currentPwd.idPassword]), 1000);
      },
      error: (err) => this.errorHandlerService.handle(err, undefined, 'modifyPasswordToast')
    });
  }
}
