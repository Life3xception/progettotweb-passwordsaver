import { Component, OnInit } from '@angular/core';
import { DetailedUserI } from '../../../shared/models/detailed-user.model';
import { ErrorHandlerService } from '../../../shared/services/error-handler.service';
import { MessageService } from 'primeng/api';
import { UsersService } from '../../../shared/services/users.service';
import { PasswordsService } from '../../../shared/services/passwords.service';
import { DetailedPasswordI } from '../../../shared/models/detailed-password.model';

@Component({
  selector: 'app-recover-passwords',
  templateUrl: './recover-passwords.component.html',
  styleUrl: './recover-passwords.component.css'
})
export class RecoverPasswordsComponent implements OnInit {
  users: DetailedUserI[] | undefined;
  selectedUser: DetailedUserI | undefined;
  usersPasswords: DetailedPasswordI[] | undefined;

  constructor(private errorHandlerService: ErrorHandlerService,
    private messageService: MessageService,
    private usersService: UsersService,
    private passwordsService: PasswordsService,
  ) { }

  ngOnInit(): void {
    this.usersService.getDetailedUsers().subscribe({
      next: (users) => {
        this.users = users;
      },
      error: (err) => this.errorHandlerService.handle(err, undefined, 'recoverPasswordsToast')
    });
  }

  seeUserPasswords(u: DetailedUserI): void {
    this.selectedUser = u;

    this.passwordsService.getDetailedDeletedPasswordsByUser(u.idUser).subscribe({
      next: (passwords) => {
        this.usersPasswords = passwords;
      },
      error: (err) => this.errorHandlerService.handle(err, undefined, 'recoverPasswordsToast') 
    });
  }

  recoverPassword(pwd: DetailedPasswordI): void {
    this.passwordsService.recoverPassword(pwd).subscribe({ // PUT
      next: () => {
        this.messageService.add({ key: 'recoverPasswordsToast',
          severity: 'success',
          summary: 'Successo',
          detail: 'Password ripristinata con successo'
        })
        setTimeout(() => window.location.reload(), 1000);
      },
      error: (err) => this.errorHandlerService.handle(err, undefined, 'recoverPasswordsToast') 
    });
  }
}
