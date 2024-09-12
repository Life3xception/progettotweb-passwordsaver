import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Service } from '../models/service.model';
import { environment } from '../../environment/environment';
import { BeApis, BeMainApis } from '../../environment/BeServlets';

@Injectable({
  providedIn: 'root'
})
export class ServicesService {

  constructor(private httpClient: HttpClient) { }

  getServices(): Observable<Service[]> {
    let url = `${environment.apiEndpoint}/${BeMainApis.services}`;
    return this.httpClient.get<Service[]>(url);
  }

  getMostUsedServicesByUser(limit: number): Observable<Service[]> {
    let url = `${environment.apiEndpoint}/${BeApis.getmostusedservicesbyuser}`;
    if(limit != 0)
      url += `?limit=${limit}`;
    return this.httpClient.get<Service[]>(url);
  }
}
