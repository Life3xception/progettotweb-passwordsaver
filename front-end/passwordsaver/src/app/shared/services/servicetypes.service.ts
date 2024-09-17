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
}
