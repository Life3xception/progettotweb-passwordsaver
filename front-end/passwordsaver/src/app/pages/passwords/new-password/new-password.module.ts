import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NewPasswordComponent } from './new-password.component';
import { ToastModule } from 'primeng/toast';
import { ReactiveFormsModule } from '@angular/forms';



@NgModule({
  declarations: [
    NewPasswordComponent
  ],
  imports: [
    CommonModule,
    ToastModule,
    ReactiveFormsModule,
  ]
})
export class NewPasswordModule { }
