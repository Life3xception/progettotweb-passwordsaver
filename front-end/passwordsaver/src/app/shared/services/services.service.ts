import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ServiceI } from '../models/service.model';
import { environment } from '../../environment/environment';
import { BeApis, BeMainApis } from '../../environment/BeServlets';
import { DetailedServiceI } from '../models/detailed-service.model';

@Injectable({
  providedIn: 'root'
})
export class ServicesService {

  constructor(private httpClient: HttpClient) { }

  getServices(): Observable<ServiceI[]> {
    let url = `${environment.apiEndpoint}/${BeMainApis.services}`;
    return this.httpClient.get<ServiceI[]>(url);
  }

  getDetailedServices(): Observable<DetailedServiceI[]> {
    let url = `${environment.apiEndpoint}/${BeApis.getdetailedservices}`;
    return this.httpClient.get<DetailedServiceI[]>(url);
  }

  getDetailedServicesByServiceType(idServType: number): Observable<DetailedServiceI[]> {
    let url = `${environment.apiEndpoint}/${BeApis.getdetailedservicesbyservicetype}?idServiceType=${idServType}`;
    return this.httpClient.get<DetailedServiceI[]>(url);
  }

  getMostUsedServicesByUser(limit: number): Observable<ServiceI[]> {
    let url = `${environment.apiEndpoint}/${BeApis.getmostusedservicesbyuser}`;
    if(limit != 0)
      url += `?limit=${limit}`;
    return this.httpClient.get<ServiceI[]>(url);
  }

  getService(idService: number): Observable<ServiceI> {
    let url = `${environment.apiEndpoint}/${BeApis.getservice}?idService=${idService}`;
    return this.httpClient.get<ServiceI>(url);
  }

  getDetailedService(idService: number): Observable<DetailedServiceI> {
    let url = `${environment.apiEndpoint}/${BeApis.getdetailedservice}?idService=${idService}`;
    return this.httpClient.get<DetailedServiceI>(url);
  }

  addService(service: ServiceI): Observable<ServiceI> {
    let url = `${environment.apiEndpoint}/${BeApis.addservice}`;
    return this.httpClient.post<ServiceI>(url, service);
  }

  updateService(service: ServiceI): Observable<any> {
    let url = `${environment.apiEndpoint}/${BeApis.updateservice}`;
    return this.httpClient.put(url, service);
  }

  deleteService(idService: number): Observable<any> {
    let url = `${environment.apiEndpoint}/${BeApis.deleteservice}?idService=${idService}`;
    return this.httpClient.delete(url);
  }
}
