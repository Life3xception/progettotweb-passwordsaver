import { Injectable } from '@angular/core';
import { AuthService } from './auth.service';
import { Router } from '@angular/router';
import { LoginResponseI } from '../models/login-response.model';

@Injectable({
  providedIn: 'root'
})
export class AuthGuardService {

  constructor(private authService: AuthService,
    private router: Router
  ) { }

  canActivate(): boolean {
    if(!this.authService.isAuthenticated()) {
      this.router.navigate(['login']);
      return false;
    }

    return true;
  }
}
