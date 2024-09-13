import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ModifyPasswordComponent } from './modify-password.component';
import { ToastModule } from 'primeng/toast';
import { ReactiveFormsModule } from '@angular/forms';



@NgModule({
  declarations: [
    ModifyPasswordComponent
  ],
  imports: [
    CommonModule,
    ToastModule,
    ReactiveFormsModule
  ]
})
export class ModifyPasswordModule { }
