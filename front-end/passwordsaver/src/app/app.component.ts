import { Component } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  title = 'PasswordSaver';

  constructor(private router: Router) {}

  // returns true if the route argument is not included in the present url
  hasNotRoute(route: string): boolean {
    return !this.router.url.includes(route);
  }
}
