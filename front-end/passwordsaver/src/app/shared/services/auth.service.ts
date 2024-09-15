import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Login } from '../models/login.model';
import { LoginData } from '../models/login-data.model';
import { Observable } from 'rxjs';
import { environment } from '../../environment/environment';
import { BeMainApis } from '../../environment/BeServlets';
import { LoginResponse } from '../models/login-response.model';
import { LocalStorageService } from './local-storage.service';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  constructor(private httpClient: HttpClient,
    private localStorageService: LocalStorageService
  ) { }

  login(login: Login): Observable<LoginResponse> {
    let url = `${environment.apiEndpoint}/${BeMainApis.login}`;
    return this.httpClient.post<LoginResponse>(url, login);
  }

  setSession(loginData: LoginData) {
    // salviamo il token in chiaro per non doverlo decifrare ad ogni richiesta
    this.localStorageService.add(environment.accessTokenName, loginData.token); 
    // salviamo anche i dati del login, cifrandoli
    this.localStorageService.addEncrypted(environment.loginDataName, JSON.stringify(loginData));
  }

  getSessionData(): LoginData | undefined {
    // recuperiamo i dati del login dal localStorage
    let data = this.localStorageService.getDecrypted(environment.loginDataName);
    // se erano settati li otterremo decriptati, altrimenti otterremo stringa vuota
    if(data != '') {
      // ritorniamo l'oggetto corrispondente ai dati
      return JSON.parse(data);
    } else {
      // altrimenti ritorniamo undefined
      return undefined;
    }
  }

  /*getUserRoles(): Roles[] | undefined {
    // recuperiamo i dati di sessione
    let login: Login|undefined = this.getSessionData();
    // e se sono presenti ritorniamo i ruoli altrimenti undefined
    return login ? login.roles : undefined;
  }*/

  logout() {
    // rimuoviamo il token
    this.localStorageService.remove(environment.accessTokenName);
    // e rimuviamo i dati di login
    this.localStorageService.remove(environment.loginDataName);
    // e mandiamo alla pagina di login
    window.location.href = 'login';
  }

  isAuthenticated(): boolean {
    // recupera il token dal localstorage (è in chiaro)
    const token = this.localStorageService.get(environment.accessTokenName);
    // controlla se il token è settato o meno e restitusce true/false
    return (token != null && token !== '');
  }

  /*isAdmin(): boolean {
    let isAdmin = false;
    // recuperiamo i ruoli dell'utente
    let roles = this.getUserRoles();
        
    // se ci sono ruoli
    if(roles) {
      // controlliamo se l'utente ha il ruolo di admin
      roles.forEach(role => {
        if(role.name === environment.adminRole)
          isAdmin = true;
      });
    }

    return isAdmin;
  }*/
}
