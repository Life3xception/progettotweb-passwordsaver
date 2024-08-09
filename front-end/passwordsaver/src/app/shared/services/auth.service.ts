import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Login } from '../models/login.model';
import { Observable } from 'rxjs';
import { environment } from '../../environment/environment';
import { BeApis, BeMainApis } from '../../environment/BeServlets';
import { LoginResponse } from '../models/login-response.model';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  constructor(private httpClient: HttpClient) { }

  login(login: Login): Observable<LoginResponse> {
    let url = `${environment.apiEndpoint}/${BeMainApis.login}`;
    return this.httpClient.post<LoginResponse>(url, login);
  }
}
