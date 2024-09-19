import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../environment/environment';
import { BeApis } from '../../environment/BeServlets';
import { DetailedPasswordI } from '../models/detailed-password.model';
import { PasswordI } from '../models/password.model';
import * as CryptoJS from 'crypto-js';
import { Base64EncodeDecode } from '../utils/base64-encode-decode';

@Injectable({
  providedIn: 'root'
})
export class PasswordsService {

  constructor(private httpClient: HttpClient,
    private base64Decoder: Base64EncodeDecode
  ) { }

  isEncoded(pwd: PasswordI): boolean {
    return this.base64Decoder.isBase64(pwd.password);
  }

  decodePassword(pwd: PasswordI): void {
    pwd.password = this.base64Decoder.decode(pwd.password);
  }

  getPassword(idPwd: number): Observable<PasswordI> {
    let url = `${environment.apiEndpoint}/${BeApis.getpassword}?idPassword=${idPwd}`;
    return this.httpClient.get<PasswordI>(url);
  }

  getDetailedPasswords(): Observable<DetailedPasswordI[]> {
    let url = `${environment.apiEndpoint}/${BeApis.getdetailedpasswords}`;
    return this.httpClient.get<DetailedPasswordI[]>(url);
  }

  getDetailedPassword(idPassword: number): Observable<DetailedPasswordI> {
    let url = `${environment.apiEndpoint}/${BeApis.getdetailedpassword}?idPassword=${idPassword}`;
    return this.httpClient.get<DetailedPasswordI>(url);
  }

  getDetailedPasswordsByService(idService: number): Observable<DetailedPasswordI[]> {
    let url = `${environment.apiEndpoint}/${BeApis.getdetailedpasswordsbyservice}?idService=${idService}`;
    return this.httpClient.get<DetailedPasswordI[]>(url);
  }

  getDetailedDeletedPasswordsByUser(idUser: number): Observable<DetailedPasswordI[]> {
    let url = `${environment.apiEndpoint}/${BeApis.getdetaileddeletedpasswordsbyuser}?idUser=${idUser}`;
    return this.httpClient.get<DetailedPasswordI[]>(url);
  }

  getDetailedStarredPasswords(limit: number): Observable<DetailedPasswordI[]> {
    let url = `${environment.apiEndpoint}/${BeApis.getdetailedstarredpasswords}`;
    if(limit != 0)
      url += `?limit=${limit}`;
    return this.httpClient.get<DetailedPasswordI[]>(url);
  }

  getDecodedPassword(idPwd: number): Observable<PasswordI> {
    let url = `${environment.apiEndpoint}/${BeApis.getdecodedpassword}?idPassword=${idPwd}`;
    return this.httpClient.get<PasswordI>(url); // FIXME: password viaggia base64 encoded, non molto sicuro
  }

  addPassword(pwd: PasswordI): Observable<PasswordI> {
    let url = `${environment.apiEndpoint}/${BeApis.addpassword}`;
    return this.httpClient.post<PasswordI>(url, pwd);
  }

  updatePassword(pwd: PasswordI): Observable<any> {
    let url = `${environment.apiEndpoint}/${BeApis.updatepassword}`;
    return this.httpClient.put(url, pwd);
  }

  recoverPassword(pwd: PasswordI): Observable<any> {
    let url = `${environment.apiEndpoint}/${BeApis.recoverpassword}`;
    return this.httpClient.put(url, pwd);
  }

  deletePassword(idPassword: number): Observable<any> {
    let url = `${environment.apiEndpoint}/${BeApis.deletepassword}?idPassword=${idPassword}`;
    return this.httpClient.delete<PasswordI>(url);
  }
}
