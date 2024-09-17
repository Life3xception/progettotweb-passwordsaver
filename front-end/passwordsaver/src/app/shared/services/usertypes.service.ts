import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { UserTypeI } from '../models/user-type.model';
import { environment } from '../../environment/environment';
import { BeApis, BeMainApis } from '../../environment/BeServlets';

@Injectable({
  providedIn: 'root'
})
export class UsertypesService {

  constructor(private httpClient: HttpClient) { }

  getUserTypes(): Observable<UserTypeI[]> {
    let url = `${environment.apiEndpoint}/${BeMainApis.usertypes}`;
    return this.httpClient.get<UserTypeI[]>(url);
  }

  getUserType(idUserType: number): Observable<UserTypeI> {
    let url = `${environment.apiEndpoint}/${BeApis.getusertype}?idUserType=${idUserType}`;
    return this.httpClient.get<UserTypeI>(url);
  }

  addUserType(userType: UserTypeI): Observable<UserTypeI> {
    let url = `${environment.apiEndpoint}/${BeApis.addusertype}`;
    return this.httpClient.post<UserTypeI>(url, userType);
  }

  updateUserType(userType: UserTypeI): Observable<any> {
    let url = `${environment.apiEndpoint}/${BeApis.updateusertype}`;
    return this.httpClient.put(url, userType);
  }

  deleteUserType(idUserType: number): Observable<any> {
    let url = `${environment.apiEndpoint}/${BeApis.deleteusertype}?idUserType=${idUserType}`;
    return this.httpClient.delete(url);
  }
}
