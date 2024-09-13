import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Service } from '../models/service.model';
import { environment } from '../../environment/environment';
import { BeApis, BeMainApis } from '../../environment/BeServlets';
import { DetailedService } from '../models/detailed-service.model';

@Injectable({
  providedIn: 'root'
})
export class ServicesService {

  constructor(private httpClient: HttpClient) { }

  getServices(): Observable<Service[]> {
    let url = `${environment.apiEndpoint}/${BeMainApis.services}`;
    return this.httpClient.get<Service[]>(url);
  }

  getDetailedServices(): Observable<DetailedService[]> {
    let url = `${environment.apiEndpoint}/${BeApis.getdetailedservices}`;
    return this.httpClient.get<DetailedService[]>(url);
  }

  getDetailedServicesByServiceType(idServType: number): Observable<DetailedService[]> {
    let url = `${environment.apiEndpoint}/${BeApis.getdetailedservicesbyservicetype}?idServiceType=${idServType}`;
    return this.httpClient.get<DetailedService[]>(url);
  }

  getMostUsedServicesByUser(limit: number): Observable<Service[]> {
    let url = `${environment.apiEndpoint}/${BeApis.getmostusedservicesbyuser}`;
    if(limit != 0)
      url += `?limit=${limit}`;
    return this.httpClient.get<Service[]>(url);
  }

  getService(idService: number): Observable<Service> {
    let url = `${environment.apiEndpoint}/${BeApis.getservice}?idService=${idService}`;
    return this.httpClient.get<Service>(url);
  }

  getDetailedService(idService: number): Observable<DetailedService> {
    let url = `${environment.apiEndpoint}/${BeApis.getdetailedservice}?idService=${idService}`;
    return this.httpClient.get<DetailedService>(url);
  }

  addService(service: Service): Observable<Service> {
    let url = `${environment.apiEndpoint}/${BeApis.addservice}`;
    return this.httpClient.post<Service>(url, service);
  }

  updateService(service: Service): Observable<any> {
    let url = `${environment.apiEndpoint}/${BeApis.updateservice}`;
    return this.httpClient.put(url, service);
  }

  deleteService(idService: number): Observable<any> {
    let url = `${environment.apiEndpoint}/${BeApis.deleteservice}?idService=${idService}`;
    return this.httpClient.delete(url);
  }
}
