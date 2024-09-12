import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { PasswordsComponent } from './passwords.component';
import { PasswordsRoutingModule } from './passwords-routing.module';
import { ToastModule } from 'primeng/toast';
import { NewPasswordModule } from './new-password/new-password.module';
import { MatIconModule } from '@angular/material/icon';


@NgModule({
  declarations: [
    PasswordsComponent
  ],
  imports: [
    CommonModule,
    PasswordsRoutingModule,
    ToastModule,
    NewPasswordModule,
    MatIconModule
  ]
})
export class PasswordsModule { }
