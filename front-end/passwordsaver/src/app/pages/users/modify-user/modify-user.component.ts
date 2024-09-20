import { Component, OnInit } from '@angular/core';
import { MessageService } from 'primeng/api';
import { ActivatedRoute, Params, Router } from '@angular/router';

@Component({
  selector: 'app-modify-user',
  templateUrl: './modify-user.component.html',
  styleUrl: './modify-user.component.css'
})
export class ModifyUserComponent implements OnInit {
  currentIdUser!: number;

  constructor(private messageService: MessageService,
    private router: Router,
    private route: ActivatedRoute
  ) { }

  ngOnInit(): void {
    // richiediamo i dati dell'utente il cui id viene passato come parametro della route
    this.route.params.subscribe((params: Params) => {
      // controlliamo che sia stato passato il parametro nella url
      if(params['idUser'] !== undefined) {
        this.currentIdUser = parseInt(params['idUser']);
      } else {
        // altrimenti redirigiamo alla home
        this.messageService.add({ 
            key: 'modifyUserToast',
            severity: 'error',
            summary: 'Errore di Pagina',
            detail: 'Parametro non valido, impossibile caricare la pagina'
        });
        setTimeout(() => this.router.navigate(['home']), 500);
      }
    });
  }
}
