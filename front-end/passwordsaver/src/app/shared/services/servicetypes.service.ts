import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ServiceTypeI } from '../models/servicetype.model';
import { environment } from '../../environment/environment';
import { BeApis, BeMainApis } from '../../environment/BeServlets';

@Injectable({
  providedIn: 'root'
})
export class ServicetypesService {

  constructor(private httpClient: HttpClient) { }

  getServiceTypes(): Observable<ServiceTypeI[]> {
    let url = `${environment.apiEndpoint}/${BeMainApis.servicetypes}`;
    return this.httpClient.get<ServiceTypeI[]>(url);
  }

  getServiceType(idServType: number): Observable<ServiceTypeI> {
    let url = `${environment.apiEndpoint}/${BeApis.getservicetype}?idServiceType=${idServType}`;
    return this.httpClient.get<ServiceTypeI>(url);
  }

  addServiceType(servType: ServiceTypeI): Observable<ServiceTypeI> {
    let url = `${environment.apiEndpoint}/${BeApis.addservicetype}`;
    return this.httpClient.post<ServiceTypeI>(url, servType);
  }

  updateServiceType(servType: ServiceTypeI): Observable<ServiceTypeI> {
    let url = `${environment.apiEndpoint}/${BeApis.updateservicetype}`;
    return this.httpClient.put<ServiceTypeI>(url, servType);
  }

  deleteServiceType(idServType: number): Observable<any> {
    let url = `${environment.apiEndpoint}/${BeApis.deleteservicetype}?idServiceType=${idServType}`;
    return this.httpClient.delete(url);
  }
}
