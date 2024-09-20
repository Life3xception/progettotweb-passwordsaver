import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { MessageService } from 'primeng/api';
import { ErrorHandlerService } from '../../../shared/services/error-handler.service';
import { UsertypesService } from '../../../shared/services/usertypes.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-usertype-form',
  templateUrl: './usertype-form.component.html',
  styleUrl: './usertype-form.component.css'
})
export class UsertypeFormComponent /*implements OnInit*/ {
  // userTypeForm!: FormGroup;

  // constructor(private formBuilder: FormBuilder,
  //   private messageService: MessageService,
  //   private errorHandlerService: ErrorHandlerService,
  //   private userTypesService: UsertypesService,
  //   private router: Router
  // ) { }

  // ngOnInit(): void {
  //   this.userTypeForm = this.formBuilder.group({
  //     'name': new FormControl('', Validators.required),
  //     'validity': new FormControl(true, Validators.required),
  //   });
  // }

  // onSubmit(event: any) {
  //   event.preventDefault();
  //   event.stopPropagation();

  //   if(!this.userTypeForm.valid)
  //     return;

  //   this.messageService.clear();

  //   let uType: UserTypeI = {
  //     idUserType: 0,
  //     name: this.userTypeForm.controls['name'].value,
  //     validity: this.userTypeForm.controls['validity'].value
  //   };

  //   this.userTypesService.addUserType(uType).subscribe({
  //     next: (response) => {
  //       this.messageService.add({ key: 'newUserTypeToast',
  //         severity: 'success',
  //         summary: 'Successo',
  //         detail: 'Tipo di Utente aggiunto con successo!'
  //       });

  //       setTimeout(() => this.router.navigate(['usertypes', 'usertype', response.idUserType]), 1000);
  //     },
  //     error: (err) => this.errorHandlerService.handle(err, undefined, 'newUserTypeToast')
  //   });
  // }
}
