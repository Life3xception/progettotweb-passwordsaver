import { Injectable } from '@angular/core';
import { AuthService } from './auth.service';
import { Router } from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class AdminGuardService {

  constructor(private authService: AuthService,
    private router: Router
  ) { }

  canActivate(): boolean {
    // se l'utente non è loggato
    if(!this.authService.isAuthenticated()) {
      // mandiamo alla pagina di login
      this.router.navigate(['login']);
      return false;
    } else {
      // altrimenti controlliamo se l'utente non è admin
      if(!this.authService.isAdmin()) {
        // in tal caso mandiamo alla pagina home
        this.router.navigate(['home']);
        return false;
      }

      // se invece è admin ritorniamo true
      return true;
    }
  }
}
