import { Component, OnInit } from '@angular/core';
import { DetailedPassword } from '../../shared/models/detailed-password.model';
import { PasswordsService } from '../../shared/services/passwords.service';
import { ErrorHandlerService } from '../../shared/services/error-handler.service';
import { ActivatedRoute, Params, Router } from '@angular/router';
import { MessageService } from 'primeng/api';

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

  constructor(private passwordsService: PasswordsService,
    private errorHandlerService: ErrorHandlerService,
    private router: Router,
    private route: ActivatedRoute,
    private messageService: MessageService
  ) { }

  ngOnInit(): void {
    this.route.params.subscribe((params: Params) => {
      this.selectedIdPassword = 0;
      this.selectedIdService = 0;
      this.showOnlyStarred = false;

      if(params['specificView'] && params['value']) { // può essere service=id oppure password=id
        const view = params['specificView'];
        const value = params['value'];
        if(view === 'service')
          this.selectedIdService = parseInt(value);
        else if(view === 'password')
          this.selectedIdPassword = parseInt(value);
        else if(view === 'starred')
          this.showOnlyStarred = value === '1';
        else {
          this.messageService.add({ 
              key: 'passwordsToast',
              severity: 'error',
              summary: 'Page Error',
              detail: 'Invalid parameter, could not load page'
          });
          setTimeout(() => this.router.navigate(['home']), 500);
        }
      }

      // console.log(this.selectedIdPassword);
      // console.log(this.selectedIdService);
      // console.log(this.showOnlyStarred);

      if(this.selectedIdPassword != 0) {

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
}
