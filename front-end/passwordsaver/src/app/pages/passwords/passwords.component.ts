import { Component, OnInit } from '@angular/core';
import { DetailedPassword } from '../../shared/models/detailed-password.model';
import { PasswordsService } from '../../shared/services/passwords.service';
import { ErrorHandlerService } from '../../shared/services/error-handler.service';

@Component({
  selector: 'app-passwords',
  templateUrl: './passwords.component.html',
  styleUrl: './passwords.component.css'
})
export class PasswordsComponent implements OnInit {
  passwords: DetailedPassword[] | undefined;

  constructor(private passwordsService: PasswordsService,
    private errorHandlerService: ErrorHandlerService,
  ) { }

  ngOnInit(): void {
    this.passwordsService.getDetailedPasswords().subscribe({
      next: (pass) => {
        this.passwords = pass;
      },
      error: (err) => this.errorHandlerService.handle(err, undefined, 'passwordsToast')
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
