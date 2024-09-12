import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { MatIconRegistry } from '@angular/material/icon';
import { ICONS } from './shared/utils/icons.costant';
import { DomSanitizer } from '@angular/platform-browser';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent implements OnInit {
  title = 'PasswordSaver';

  constructor(private router: Router,
    private iconRegistry: MatIconRegistry, 
    private sanitizer: DomSanitizer
  ) {}

  // returns true if the route argument is not included in the present url
  hasNotRoute(route: string): boolean {
    return !this.router.url.includes(route);
  }

  ngOnInit(): void {
    this.loadIcons();
  }

  // metodo per registrare gli svg che abbiamo nella cartella svg-icons
  // come maticons e poterle utilizzare nell'applicazione
  // serve il DomSanitizer per protezione contro XSS negli url delle svg
  private loadIcons(): void {
    ICONS.forEach(icon => this.iconRegistry.addSvgIcon(icon.name, 
      this.sanitizer.bypassSecurityTrustResourceUrl(icon.path)));
  }
}
