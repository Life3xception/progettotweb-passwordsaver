import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../environment/environment';
import { BeApis } from '../../environment/BeServlets';
import { DetailedPassword } from '../models/detailed-password.model';
import { Password } from '../models/password.model';
import * as CryptoJS from 'crypto-js';
import { Base64EncodeDecode } from '../utils/base64-encode-decode';

@Injectable({
  providedIn: 'root'
})
export class PasswordsService {

  constructor(private httpClient: HttpClient,
    private base64Decoder: Base64EncodeDecode
  ) { }

  isEncoded(pwd: Password): boolean {
    return this.base64Decoder.isBase64(pwd.password);
  }

  decodePassword(pwd: Password): void {
    pwd.password = this.base64Decoder.decode(pwd.password);
  }

  getPassword(idPwd: number): Observable<Password> {
    let url = `${environment.apiEndpoint}/${BeApis.getpassword}?idPassword=${idPwd}`;
    return this.httpClient.get<Password>(url);
  }

  getDetailedPasswords(): Observable<DetailedPassword[]> {
    let url = `${environment.apiEndpoint}/${BeApis.getdetailedpasswords}`;
    return this.httpClient.get<DetailedPassword[]>(url);
  }

  getDetailedPassword(idPassword: number): Observable<DetailedPassword> {
    let url = `${environment.apiEndpoint}/${BeApis.getdetailedpassword}?idPassword=${idPassword}`;
    return this.httpClient.get<DetailedPassword>(url);
  }

  getDetailedPasswordsByService(idService: number): Observable<DetailedPassword[]> {
    let url = `${environment.apiEndpoint}/${BeApis.getdetailedpasswordsbyservice}?idService=${idService}`;
    return this.httpClient.get<DetailedPassword[]>(url);
  }

  getDetailedStarredPasswords(limit: number): Observable<DetailedPassword[]> {
    let url = `${environment.apiEndpoint}/${BeApis.getdetailedstarredpasswords}`;
    if(limit != 0)
      url += `?limit=${limit}`;
    return this.httpClient.get<DetailedPassword[]>(url);
  }

  getDecodedPassword(idPwd: number): Observable<Password> {
    let url = `${environment.apiEndpoint}/${BeApis.getdecodedpassword}?idPassword=${idPwd}`;
    return this.httpClient.get<Password>(url); // FIXME: in questo modo la password viaggia in chiaro, molto male!!!
  }

  addPassword(pwd: Password): Observable<Password> {
    let url = `${environment.apiEndpoint}/${BeApis.addpassword}`;
    return this.httpClient.post<Password>(url, pwd);
  }

  modifyPassword(pwd: Password): Observable<any> {
    let url = `${environment.apiEndpoint}/${BeApis.updatepassword}`;
    return this.httpClient.put(url, pwd);
  }
}
