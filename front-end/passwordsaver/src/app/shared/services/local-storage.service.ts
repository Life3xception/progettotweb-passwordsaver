import { Injectable } from '@angular/core';
import { environment } from '../../environment/environment';
import * as CryptoJS from 'crypto-js';

@Injectable({
  providedIn: 'root'
})
export class LocalStorageService {

  constructor() { }

  add(key: string, value: string) {
    localStorage.setItem(key, value);
  }

  addEncrypted(key: string, value: string) {
    this.add(key, CryptoJS.TripleDES.encrypt(value, environment.key).toString());
  }

  get(key: string): string {
    return localStorage.getItem(key) ?? '';
  }

  getDecrypted(key: string): string {
    let data = this.get(key);
    
    if(data == '')
      return '';

    return CryptoJS.TripleDES.decrypt(
      data, 
      environment.key
    ).toString(CryptoJS.enc.Utf8);
  }

  remove(key: string) {
    localStorage.removeItem(key);
  }
}
