import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { DetailedUserI } from '../models/detailed-user.model';
import { environment } from '../../environment/environment';
import { BeApis } from '../../environment/BeServlets';
import { UserI } from '../models/user.model';

@Injectable({
  providedIn: 'root'
})
export class UsersService {

  constructor(private httpClient: HttpClient) { }

  getDetailedUsers(): Observable<DetailedUserI[]> {
    let url = `${environment.apiEndpoint}/${BeApis.getdetailedusers}`;
    return this.httpClient.get<DetailedUserI[]>(url);
  }

  getUser(idUser: number): Observable<UserI> {
    let url = `${environment.apiEndpoint}/${BeApis.getuser}?idUser=${idUser}`;
    return this.httpClient.get<UserI>(url);
  }

  getDetailedUser(idUser: number): Observable<DetailedUserI> {
    let url = `${environment.apiEndpoint}/${BeApis.getdetaileduser}?idUser=${idUser}`;
    return this.httpClient.get<DetailedUserI>(url);
  }

  getDetailedUsersByUserType(idUserType: number): Observable<DetailedUserI[]> {
    let url = `${environment.apiEndpoint}/${BeApis.getdetailedusersbyusertype}?idUserType=${idUserType}`;
    return this.httpClient.get<DetailedUserI[]>(url);
  }

  addUser(user: UserI): Observable<UserI> {
    let url = `${environment.apiEndpoint}/${BeApis.adduser}`;
    return this.httpClient.post<UserI>(url, user);
  }

  updateUser(user: UserI): Observable<any> {
    let url = `${environment.apiEndpoint}/${BeApis.updateuser}`;
    return this.httpClient.put(url, user);
  }

  deleteUser(idUser: number): Observable<any> {
    let url = `${environment.apiEndpoint}/${BeApis.deleteuser}?idUser=${idUser}`;
    return this.httpClient.delete(url);
  }
}
