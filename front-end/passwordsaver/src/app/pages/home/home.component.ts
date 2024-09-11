import { Component, OnInit } from '@angular/core';
import { PasswordsService } from '../../shared/services/passwords.service';
import { ErrorHandlerService } from '../../shared/services/error-handler.service';
import { DetailedPassword } from '../../shared/models/detailed-password.model';
import { ServicesService } from '../../shared/services/services.service';
import { Service } from '../../shared/models/service.model';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
})
export class HomeComponent implements OnInit {
  starred: DetailedPassword[] | undefined;
  usedServices: Service[] | undefined;
  showedStarredPwds = 3;
  showedUsedServices = 3;

  constructor(private passwordsService: PasswordsService,
    private servicesService: ServicesService,
    private errorHandlerService: ErrorHandlerService
  ) { }

  ngOnInit(): void {
    this.passwordsService.getDetailedStarredPasswords(this.showedStarredPwds).subscribe({
      next: (passwords) => {
        this.starred = passwords;
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

  getPasswordDetail(pwd: DetailedPassword): void {
    // TODO: implementare router navigation a pagina dettaglio password
    //this.router.navigate(['getpassword']) // qualcosa di simile
    console.log(`GET PASSWORD DETAIL ID: ${pwd.idPassword}`);
  }

  getPasswordsByService(service: Service): void {
    // TODO: implementare router navigation a lista password visualizzate per servizio
    //this.router.navigate(['getpasswordsbyservice']) // qualcosa di simile
    console.log(`GET SERVICE DETAIL ID: ${service.idService}`);
  }

  seeMore(section: string): void {
    // TODO: implementare redirect a lista pwd starred se section == 'starred', 
    // invece se è 'services' allora andare a lista servizi usati da utente
    console.log(`SEE MORE ${section}`);
  }
}
