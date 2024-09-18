import { Component, OnInit } from '@angular/core';
import { PasswordsService } from '../../shared/services/passwords.service';
import { ErrorHandlerService } from '../../shared/services/error-handler.service';
import { DetailedPasswordI } from '../../shared/models/detailed-password.model';
import { ServicesService } from '../../shared/services/services.service';
import { ServiceI } from '../../shared/models/service.model';
import { Router } from '@angular/router';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
})
export class HomeComponent implements OnInit {
  starredPasswords: DetailedPasswordI[] | undefined;
  usedServices: ServiceI[] | undefined;
  showedStarredPwds = 3;
  showedUsedServices = 3;

  constructor(private passwordsService: PasswordsService,
    private servicesService: ServicesService,
    private errorHandlerService: ErrorHandlerService,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.passwordsService.getDetailedStarredPasswords(this.showedStarredPwds).subscribe({
      next: (passwords) => {
        this.starredPasswords = passwords;
      },
      error: (err) => this.errorHandlerService.handle(err, undefined, 'homeToast') 
    });

    this.servicesService.getMostUsedServicesByUser(this.showedUsedServices).subscribe({
      next: (services) => {
        this.usedServices = services;
      },
      error: (err) => this.errorHandlerService.handle(err, undefined, 'homeToast') 
    });
  }

  getPasswordDetail(pwd: DetailedPasswordI): void {
    // router navigation a pagina dettaglio password
    this.router.navigate(['passwords', 'password', pwd.idPassword]);
  }

  getPasswordsByService(service: ServiceI): void {
    // router navigation a lista password visualizzate per servizio
    this.router.navigate(['passwords', 'service', service.idService]);
  }

  seeMore(section: string): void {
    // redirect a lista pwd starred se section == 'starred', 
    // invece se è 'services' allora a lista servizi usati da utente
    if(section === 'starred') 
      this.router.navigate(['passwords', 'starred', '1']);
    else if(section == 'services')
      this.router.navigate(['services']);
  }
}
