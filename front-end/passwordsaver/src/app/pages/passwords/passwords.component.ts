import { Component, OnInit } from '@angular/core';
import { DetailedPasswordI } from '../../shared/models/detailed-password.model';
import { PasswordsService } from '../../shared/services/passwords.service';
import { ErrorHandlerService } from '../../shared/services/error-handler.service';
import { ActivatedRoute, Params, Router } from '@angular/router';
import { MessageService } from 'primeng/api';
import { ServiceI } from '../../shared/models/service.model';
import { ServicesService } from '../../shared/services/services.service';

@Component({
  selector: 'app-passwords',
  templateUrl: './passwords.component.html',
  styleUrl: './passwords.component.css'
})
export class PasswordsComponent implements OnInit {
  passwords: DetailedPasswordI[] | undefined;
  selectedIdService: number | undefined;
  selectedIdPassword: number | undefined;
  showOnlyStarred: boolean = false;
  pageTitle: string = 'Lista delle Password';
  services: ServiceI[] | undefined;
  pwdToDelete: DetailedPasswordI | undefined;

  constructor(private passwordsService: PasswordsService,
    private errorHandlerService: ErrorHandlerService,
    private router: Router,
    private route: ActivatedRoute,
    private messageService: MessageService,
    private servicesService: ServicesService
  ) { }

  ngOnInit(): void {
    this.servicesService.getServices().subscribe({
      next: (services) => {
        this.services = services;
      },
      error: (err) => this.errorHandlerService.handle(err, undefined, 'passwordsToast')
    });

    this.route.params.subscribe((params: Params) => {
      this.selectedIdPassword = 0;
      this.selectedIdService = 0;
      this.showOnlyStarred = false;
      this.pageTitle = 'Lista delle Password';

      if(params['specificView'] && params['value']) { // può essere service=id oppure password=id
        const view = params['specificView'];
        const value = params['value'];
        if(view === 'service') {
          this.selectedIdService = parseInt(value);
          this.pageTitle = 'Lista Password per Servizio';
        }
        else if(view === 'password') {
          this.selectedIdPassword = parseInt(value);
          this.pageTitle = 'Dettaglio Password';
        }
        else if(view === 'starred') {
          this.showOnlyStarred = value === '1';
          this.pageTitle = 'Lista Password Preferite';
        }
        else {
          this.messageService.add({ 
              key: 'passwordsToast',
              severity: 'error',
              summary: 'Errore di Pagina',
              detail: 'Parametro non valido, impossibile caricare la pagina'
          });
          setTimeout(() => this.router.navigate(['home']), 500);
        }
      }

      if(this.selectedIdPassword != 0) {
        this.passwordsService.getDetailedPassword(this.selectedIdPassword).subscribe({
          next: (pass) => {
            this.passwords = [pass];
          },
          error: (err) => this.errorHandlerService.handle(err, undefined, 'passwordsToast')
        });
      } else if(this.selectedIdService != 0) {
        this.passwordsService.getDetailedPasswordsByService(this.selectedIdService).subscribe({
          next: (pass) => {
            this.passwords = pass;
          },
          error: (err) => this.errorHandlerService.handle(err, undefined, 'passwordsToast')
        });
      } else if(this.showOnlyStarred) {
        this.passwordsService.getDetailedStarredPasswords(0).subscribe({
          next: (pass) => {
            this.passwords = pass;
          },
          error: (err) => this.errorHandlerService.handle(err, undefined, 'passwordsToast')
        });
      } else {
        this.passwordsService.getDetailedPasswords().subscribe({
          next: (pass) => {
            this.passwords = pass;
          },
          error: (err) => this.errorHandlerService.handle(err, undefined, 'passwordsToast')
        }); 
      }
    });
  }

  goToNewPassword(): void {
    this.router.navigate(['passwords', 'new-password']);
  }

  showStarred(): void {
    if(!this.showOnlyStarred)
      this.router.navigate(['passwords', 'starred', '1']);
    else
      this.router.navigate(['passwords']);
  }

  showSelectedService(idService: string): void {
    if(idService !== '0')
      this.router.navigate(['passwords', 'service', idService]);
    else
      this.router.navigate(['passwords']);
  }

  showPassword(pwd: DetailedPasswordI): void {
    if(!pwd.showed) {
      if(this.passwordsService.isEncoded(pwd)) {
        this.passwordsService.getDecodedPassword(pwd.idPassword).subscribe({
          next: (decodedPwd) => {
            this.passwordsService.decodePassword(decodedPwd);
            pwd.password = decodedPwd.password;
            pwd.showed = true;
          },
          error: (err) => this.errorHandlerService.handle(err, undefined, 'passwordsToast')
        });
      } else {
        pwd.showed = true;
      }
    } else {
      pwd.showed = false;
    }
  }

  modifyPassword(pwd: DetailedPasswordI): void {
    this.router.navigate(['passwords', 'modify-password', pwd.idPassword]);
  }

  deletePassword(pwd: DetailedPasswordI): void {
    this.pwdToDelete = pwd;
  }
  
  confirmDeletePassword(): void {
    if(this.pwdToDelete !== undefined) {
      this.passwordsService.deletePassword(this.pwdToDelete.idPassword).subscribe({
        next: () => {
          this.messageService.add({ key: 'passwordsToast',
            severity: 'success',
            summary: 'Successo',
            detail: 'Password eliminata con successo'
          });
          
          if(this.selectedIdPassword != 0) {
            // eravamo in pagina dettaglio password, dobbiamo tornare alla lista password
            setTimeout(() => window.location.href = 'passwords', 500);
          } else  {
            // eravamo in pagina password by service/ password preferite / lista password, dobbiamo ricaricare la pagina
            setTimeout(() => window.location.reload(), 500);
          }
        },
        error: (err) => this.errorHandlerService.handle(err, undefined, 'passwordsToast')
      });
    }
  }
}
