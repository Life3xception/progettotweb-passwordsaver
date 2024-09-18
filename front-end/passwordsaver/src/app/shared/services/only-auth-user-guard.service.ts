import { Injectable } from '@angular/core';
import { AuthService } from './auth.service';
import { ActivatedRouteSnapshot, Router, RouterStateSnapshot } from '@angular/router';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class OnlyAuthUserGuardService {

  constructor(private authService: AuthService,
    private router: Router
  ) { }

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<boolean> | Promise<boolean> | boolean {
    // se l'utente non è loggato
    if(!this.authService.isAuthenticated()) {
      // mandiamo alla pagina di login
      this.router.navigate(['login']);
      return false;
    } else {
      // altrimenti controlliamo se l'utente non è admin o se non è l'utente di cui si richiede la modifica

      // Recuperiamo i parametri dalla querystring
      const params = route.params;
      const idUser = params['idUser'];

      if(!this.authService.isAdmin() && this.authService.getSessionData()?.idUser != idUser) {
        // in tal caso mandiamo alla pagina home
        this.router.navigate(['home']);
        return false;
      }

      // se invece è admin ritorniamo true
      return true;
    }
  }
}
