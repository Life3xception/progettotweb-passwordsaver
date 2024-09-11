import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { PasswordsComponent } from './passwords.component';
import { PasswordsRoutingModule } from './passwords-routing.module';
import { ToastModule } from 'primeng/toast';


@NgModule({
  declarations: [
    PasswordsComponent
  ],
  imports: [
    CommonModule,
    PasswordsRoutingModule,
    ToastModule
  ]
})
export class PasswordsModule { }
