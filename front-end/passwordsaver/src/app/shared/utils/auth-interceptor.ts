import { HttpEvent, HttpHandler, HttpInterceptor, HttpRequest } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";


import { LocalStorageService } from "../services/local-storage.service";
import { environment } from "../../environment/environment";

@Injectable()
export class AuthInterceptor implements HttpInterceptor {
  constructor(private localStorageService: LocalStorageService) { }

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    // recuperiamo il token
    const token = this.localStorageService.get(environment.accessTokenName);

    // se il token è settato
    if(token) {
        // cloniamo la richiesta aggiungendo l'header Authorization con il token
        req = req.clone({
            headers: req.headers.set("Authorization", `Bearer ${token}`)
        });
    }

    return next.handle(req);
  }
}