import { Component, OnInit } from '@angular/core';
import { MessageService } from 'primeng/api';
import { ActivatedRoute, Params, Router } from '@angular/router';

@Component({
  selector: 'app-modify-usertype',
  templateUrl: './modify-usertype.component.html',
  styleUrl: './modify-usertype.component.css'
})
export class ModifyUsertypeComponent implements OnInit {
  currentIdUserType!: number;

  constructor(private messageService: MessageService,
    private router: Router,
    private route: ActivatedRoute
  ) { }

  ngOnInit(): void {
    // richiediamo i dati del tipo di utente il cui id viene passato come parametro della route
    this.route.params.subscribe((params: Params) => {
      // controlliamo che sia stato passato il parametro nella url
      if(params['idUserType'] !== undefined) {
        this.currentIdUserType = parseInt(params["idUserType"]);
      } else {
        // altrimenti redirigiamo alla home
        this.messageService.add({ 
            key: 'modifyUserTypeToast',
            severity: 'error',
            summary: 'Errore di Pagina',
            detail: 'Parametro non valido, impossibile caricare la pagina'
        });
        setTimeout(() => this.router.navigate(['home']), 500);
      }
    });
  }
}
