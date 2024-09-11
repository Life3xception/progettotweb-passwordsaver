import { Component, OnInit } from '@angular/core';
import { PasswordsService } from '../../shared/services/passwords.service';
import { Password } from '../../shared/models/password.model';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
})
export class HomeComponent implements OnInit {
  starred: Password[] | undefined;
  showedStarredPwds = 3;

  constructor(private passwordsService: PasswordsService) { }

  ngOnInit(): void {
    this.passwordsService.getStarredPasswords(this.showedStarredPwds).subscribe({
      next: (passwords) => {
        this.starred = passwords;
      },
      error: (err) => console.log(err) // TODO: mettere errorhandler comune che in caso di 401/403 mandi a login!!!!!!
    });
  }
}
