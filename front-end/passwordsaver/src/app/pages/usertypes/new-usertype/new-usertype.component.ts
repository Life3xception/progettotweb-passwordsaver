import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { MessageService } from 'primeng/api';
import { ErrorHandlerService } from '../../../shared/services/error-handler.service';
import { UsertypesService } from '../../../shared/services/usertypes.service';
import { Router } from '@angular/router';
import { UserTypeI } from '../../../shared/models/user-type.model';

@Component({
  selector: 'app-new-usertype',
  templateUrl: './new-usertype.component.html',
  styleUrl: './new-usertype.component.css'
})
export class NewUsertypeComponent implements OnInit {
  newUserTypeForm!: FormGroup;

  constructor(private formBuilder: FormBuilder,
    private messageService: MessageService,
    private errorHandlerService: ErrorHandlerService,
    private userTypesService: UsertypesService,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.newUserTypeForm = this.formBuilder.group({
      'name': new FormControl('', Validators.required),
      'validity': new FormControl(true, Validators.required),
    });
  }

  onSubmit(event: any) {
    event.preventDefault();
    event.stopPropagation();

    if(!this.newUserTypeForm.valid)
      return;

    this.messageService.clear();

    let uType: UserTypeI = {
      idUserType: 0,
      name: this.newUserTypeForm.controls['name'].value,
      validity: this.newUserTypeForm.controls['validity'].value
    };

    this.userTypesService.addUserType(uType).subscribe({
      next: (response) => {
        this.messageService.add({ key: 'newUserTypeToast',
          severity: 'success',
          summary: 'Successo',
          detail: 'Tipo di Utente aggiunto con successo!'
        });

        setTimeout(() => this.router.navigate(['usertypes', 'usertype', response.idUserType]), 1000);
      },
      error: (err) => this.errorHandlerService.handle(err, undefined, 'newUserTypeToast')
    });
  }
}
