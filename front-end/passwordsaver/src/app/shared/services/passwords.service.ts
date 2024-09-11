import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Password } from '../models/password.model';
import { environment } from '../../environment/environment';
import { BeApis, BeMainApis } from '../../environment/BeServlets';

@Injectable({
  providedIn: 'root'
})
export class PasswordsService {

  constructor(private httpClient: HttpClient) { }

  getStarredPasswords(limit: number): Observable<Password[]> {
    let url = `${environment.apiEndpoint}/${BeApis.getstarredpasswords}`;
    if(limit != 0)
      url += `?limit=${limit}`;
    return this.httpClient.get<Password[]>(url);
  }
}
