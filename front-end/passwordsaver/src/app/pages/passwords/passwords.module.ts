import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { PasswordsComponent } from './passwords.component';
import { PasswordsRoutingModule } from './passwords-routing.module';
import { ToastModule } from 'primeng/toast';
import { MatIconModule } from '@angular/material/icon';
import { ModifyPasswordComponent } from './modify-password/modify-password.component';
import { NewPasswordComponent } from './new-password/new-password.component';
import { ReactiveFormsModule } from '@angular/forms';
import { RecoverPasswordsComponent } from './recover-passwords/recover-passwords.component';

@NgModule({
  declarations: [
    PasswordsComponent,
    NewPasswordComponent,
    ModifyPasswordComponent,
    RecoverPasswordsComponent
  ],
  imports: [
    CommonModule,
    PasswordsRoutingModule,
    ToastModule,
    MatIconModule,
    ReactiveFormsModule
  ]
})
export class PasswordsModule { }
