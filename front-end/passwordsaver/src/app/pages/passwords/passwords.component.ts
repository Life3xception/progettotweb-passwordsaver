import { Component, OnInit } from '@angular/core';
import { DetailedPassword } from '../../shared/models/detailed-password.model';
import { PasswordsService } from '../../shared/services/passwords.service';
import { ErrorHandlerService } from '../../shared/services/error-handler.service';
import { ActivatedRoute, Params, Router } from '@angular/router';
import { MessageService } from 'primeng/api';
import { Service } from '../../shared/models/service.model';
import { ServicesService } from '../../shared/services/services.service';

@Component({
  selector: 'app-passwords',
  templateUrl: './passwords.component.html',
  styleUrl: './passwords.component.css'
})
export class PasswordsComponent implements OnInit {
  passwords: DetailedPassword[] | undefined;
  selectedIdService: number | undefined;
  selectedIdPassword: number | undefined;
  showOnlyStarred: boolean = false;
  pageTitle: string = 'Lista delle Password';
  services: Service[] | undefined;

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

  showPassword(pwd: DetailedPassword): void {
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

  modifyPassword(pwd: DetailedPassword): void {
    this.router.navigate(['passwords', 'modify-password', pwd.idPassword]);
  }
  
  deletePassword(pwd: DetailedPassword): void {
    console.log("DELETE"); // TODO
  }
}
