import { Component, OnInit } from '@angular/core';
import { PasswordsService } from '../../shared/services/passwords.service';
import { Password } from '../../shared/models/password.model';
import { ErrorHandlerService } from '../../shared/services/error-handler.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
})
export class HomeComponent implements OnInit {
  starred: Password[] | undefined;
  showedStarredPwds = 3;

  constructor(private passwordsService: PasswordsService,
    private errorHandlerService: ErrorHandlerService
  ) { }

  ngOnInit(): void {
    this.passwordsService.getStarredPasswords(this.showedStarredPwds).subscribe({
      next: (passwords) => {
        this.starred = passwords;
      },
      error: (err) => this.errorHandlerService.handle(err, undefined, 'homeToast') 
    });
  }
}
