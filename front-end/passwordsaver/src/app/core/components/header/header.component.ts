import { Component, OnInit } from '@angular/core';
import { NavigationEnd, Router } from '@angular/router';
import { AuthService } from '../../../shared/services/auth.service';

// Custom TS type that represents a menu item
type MenuItem = {
  text: string,
  url: string
};

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrl: './header.component.css'
})
export class HeaderComponent implements OnInit {
  menuItems: MenuItem[] = [
    {
      text: 'Home',
      url: 'home'
    },
    {
      text: 'Lista Password',
      url: 'passwords'
    },
    {
      text: 'Lista Servizi',
      url: 'services'
    }
  ];
  selectedMenuItem: MenuItem | undefined;

  constructor(private router: Router,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    // events è uno stream observable per gli eventi del router
    // l'evento RouterEvent contiene un parametro url che contiene
    // l'url attiva, se presente lo utilizziamo per determinare il titolo del header
    this.router.events.subscribe((event: any) => {
      // se è settato ed è di tipo NavigationEnd (ci sono più eventi di navigazione, 
      // NavigationStart, NavigationCancel, NavigationEnd, ...)
      if(event instanceof NavigationEnd && event.url) {
        // ad ogni cambiamento di route, cancelliamo il selectedItem
        this.selectedMenuItem = undefined;

        // impostiamo come selezionato il menuitem con proprietà url contenuta nell'url della route, se esiste
        this.selectedMenuItem = this.menuItems.find((x: MenuItem) => event.url.slice(1).includes(x.url));

        // nel caso non abbiamo trovato il menuitem e la url è /, allora settiamo attivo home
        if(!this.selectedMenuItem && event.url === '/')
          this.selectedMenuItem = this.menuItems.find(x => x.url === 'home');
      }
    });
  }

  handleClick(item: MenuItem): void {
    this.router.navigate([item.url]).then((success) =>  {
      if(success) this.selectedMenuItem = item;
    });
  }

  isSelected(item: MenuItem): boolean {
    return item === this.selectedMenuItem;
  }

  logout(): void {
    this.authService.logout();
  }
}
