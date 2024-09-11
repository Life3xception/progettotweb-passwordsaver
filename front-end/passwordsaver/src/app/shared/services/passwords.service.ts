import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../environment/environment';
import { BeApis } from '../../environment/BeServlets';
import { DetailedPassword } from '../models/detailed-password.model';

@Injectable({
  providedIn: 'root'
})
export class PasswordsService {

  constructor(private httpClient: HttpClient) { }

  getDetailedStarredPasswords(limit: number): Observable<DetailedPassword[]> {
    let url = `${environment.apiEndpoint}/${BeApis.getdetailedstarredpasswords}`;
    if(limit != 0)
      url += `?limit=${limit}`;
    return this.httpClient.get<DetailedPassword[]>(url);
  }
}
