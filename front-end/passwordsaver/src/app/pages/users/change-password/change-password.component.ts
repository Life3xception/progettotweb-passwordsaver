import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { TwoControlsMatchingValidatorDirective } from '../../../shared/directives/two-controls-matching-validator.directive';
import { environment } from '../../../environment/environment';
import { UserI } from '../../../shared/models/user.model';
import { MessageService } from 'primeng/api';
import { ErrorHandlerService } from '../../../shared/services/error-handler.service';
import { UsersService } from '../../../shared/services/users.service';
import { ActivatedRoute, Params, Router } from '@angular/router';
import { AuthService } from '../../../shared/services/auth.service';

@Component({
  selector: 'app-change-password',
  templateUrl: './change-password.component.html',
  styleUrl: './change-password.component.css'
})
export class ChangePasswordComponent implements OnInit {
  changePwdForm!: FormGroup;
  currentUser!: UserI;

  constructor(private messageService: MessageService,
    private formBuilder: FormBuilder,
    private errorHandlerService: ErrorHandlerService,
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
              
            this.changePwdForm = this.formBuilder.group({
              'password': new FormControl('', Validators.compose([Validators.required, Validators.pattern(environment.pwdRegex)])),
              'confirmPassword': new FormControl('', Validators.compose([Validators.required, Validators.pattern(environment.pwdRegex)]))
            }, {
              // validators permette di passare dei validatori aggiuntivi a livello di formgroup
              // gli passiamo il validator che controlla se due campi corrispondono
              validators: TwoControlsMatchingValidatorDirective.twoControlsMatchingValidator('password', 'confirmPassword', 'Passwords don\'t match.')
            });
          },
          error: (err) => this.errorHandlerService.handle(err, undefined, 'changePwdToast')
        });
      } else {
        // altrimenti redirigiamo alla home
        this.messageService.add({ 
            key: 'changePwdToast',
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

    if(!this.changePwdForm.valid)
      return;

    this.messageService.clear();
    
    // FIXME: DEVO CRIPTARLA (?)
    this.currentUser.password = this.changePwdForm.controls['password'].value;

    this.usersService.changePassword(this.currentUser).subscribe({
      next: () => {
        this.messageService.add({ key: 'changePwdToast',
          severity: 'success',
          summary: 'Successo',
          detail: 'Password recuperata con successo'
        });

        // dobbiamo fare logout perché l'utente deve riloggarsi
        setTimeout(() => this.authService.logout(), 1000);
      },
      error: (err) => this.errorHandlerService.handle(err, undefined, 'changePwdToast')
    });
  }

  isPwdValid(pwd: string): boolean {
    return new RegExp(environment.pwdRegex).test(pwd);
  }
}
