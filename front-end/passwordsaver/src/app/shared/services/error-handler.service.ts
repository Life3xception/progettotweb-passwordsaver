import { HttpErrorResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { throwError } from 'rxjs';

import { Message, MessageService } from 'primeng/api';
import { AuthService } from './auth.service';

@Injectable({
  providedIn: 'root'
})
export class ErrorHandlerService {

  constructor(private router: Router,
    private messageService: MessageService,
    private authService: AuthService
  ) { }

  handle(error: any, toastMessage?: Message, toastKey?: string): void {
    console.log(error); // TO REMOVE (?)
    if(error instanceof HttpErrorResponse) {
      if(error.status === 401 && !this.router.url.includes('login')) {
        this.authService.logout(); // dobbiamo fare logout, altrimenti ci manda a login che ci manda a home
        window.location.href  = '/login'; // per forzare il ricaricamento della pagine e quindi dei componenti
      } else if(error.status === 403) {
        // reindirizziamo alla pagina home
        setTimeout(() => this.router.navigate(['home']), 1000);
      } else if(error.error && error.error.error && toastKey) {
        this.messageService.add({key: toastKey,
            severity: 'error',
            summary: error.error.error,
            detail: error.error.message,
            sticky: true
          });
      } else if(error.status && toastKey) {
        this.messageService.add({key: toastKey,
            severity: 'error',
            summary: `Error ${error.status}`,
            detail: `${error.statusText}`,
            sticky: true
          });
      } else {
        throwError(() => new Error('An error occured'));
      }
    }
  }
}
