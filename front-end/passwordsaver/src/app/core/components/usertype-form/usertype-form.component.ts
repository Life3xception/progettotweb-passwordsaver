import { Component, input, Input, OnInit } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { MessageService } from 'primeng/api';
import { ErrorHandlerService } from '../../../shared/services/error-handler.service';
import { UsertypesService } from '../../../shared/services/usertypes.service';
import { Router } from '@angular/router';
import { UserTypeI } from '../../../shared/models/user-type.model';

@Component({
  selector: 'app-usertype-form',
  templateUrl: './usertype-form.component.html',
  styleUrl: './usertype-form.component.css'
})
export class UsertypeFormComponent implements OnInit {
  userTypeForm!: FormGroup;
  currentUserType!: UserTypeI;
  @Input() page!: string;
  @Input() idUserType!: number;
  @Input() toastKey!: string;
  @Input() title!: string;
  @Input() submitButton!: string;

  constructor(private formBuilder: FormBuilder,
    private messageService: MessageService,
    private errorHandlerService: ErrorHandlerService,
    private userTypesService: UsertypesService,
    private router: Router
  ) { }

  ngOnInit(): void {
    if(this.page === 'new-usertype') {
      this.userTypeForm = this.formBuilder.group({
        'name': new FormControl('', Validators.required),
        'validity': new FormControl(true, Validators.required),
      });
    } else if(this.page === 'modify-usertype') {
      this.userTypesService.getUserType(this.idUserType).subscribe({
        next: (uType) => {
          this.currentUserType = uType;
      
          this.userTypeForm = this.formBuilder.group({
            'name': new FormControl(this.currentUserType.name, Validators.required),
            'validity': new FormControl(this.currentUserType.validity, Validators.required),
          });
        },
        error: (err) => this.errorHandlerService.handle(err, undefined, this.toastKey)
      });
    }
  }

  onSubmit(event: any) {
    event.preventDefault();
    event.stopPropagation();

    if(!this.userTypeForm.valid)
      return;

    this.messageService.clear();
    if(this.page === 'new-usertype') {
      this.addUserType();
    } else if(this.page === 'modify-usertype') {
      this.updateUserType();
    }
    
  }

  addUserType(): void {
    let uType: UserTypeI = {
      idUserType: 0,
      name: this.userTypeForm.controls['name'].value,
      validity: this.userTypeForm.controls['validity'].value
    };

    this.userTypesService.addUserType(uType).subscribe({
      next: (response) => {
        this.messageService.add({ key: this.toastKey,
          severity: 'success',
          summary: 'Successo',
          detail: 'Tipo di Utente aggiunto con successo!'
        });

        setTimeout(() => this.router.navigate(['usertypes', 'usertype', response.idUserType]), 1000);
      },
      error: (err) => this.errorHandlerService.handle(err, undefined, this.toastKey)
    });
  }

  updateUserType(): void {
    this.currentUserType.name = this.userTypeForm.controls['name'].value;
    this.currentUserType.validity = this.userTypeForm.controls['validity'].value;

    this.userTypesService.updateUserType(this.currentUserType).subscribe({
      next: () => {
        this.messageService.add({ key: this.toastKey,
          severity: 'success',
          summary: 'Successo',
          detail: 'Tipo di Utente modificato con successo'
        });

        setTimeout(() => this.router.navigate(['usertypes', 'usertype', this.currentUserType.idUserType]), 1000);
      },
      error: (err) => this.errorHandlerService.handle(err, undefined, this.toastKey)
    });
  }
}
