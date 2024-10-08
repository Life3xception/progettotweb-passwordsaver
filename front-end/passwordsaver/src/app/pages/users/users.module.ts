import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { UsersRoutingModule } from './users-routing.module';
import { UsersComponent } from './users.component';
import { ToastModule } from 'primeng/toast';
import { ReactiveFormsModule } from '@angular/forms';
import { MatIconModule } from '@angular/material/icon';
import { NewUserComponent } from './new-user/new-user.component';
import { ModifyUserComponent } from './modify-user/modify-user.component';
import { ChangePasswordComponent } from './change-password/change-password.component';
import { CoreModule } from '../../core/core.module';



@NgModule({
  declarations: [
    UsersComponent,
    NewUserComponent,
    ModifyUserComponent,
    ChangePasswordComponent
  ],
  imports: [
    CommonModule,
    ToastModule,
    ReactiveFormsModule,
    MatIconModule,
    UsersRoutingModule,
    CoreModule
  ]
})
export class UsersModule { }
