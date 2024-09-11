import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { LoginModule } from './login/login.module';
import { HomeModule } from './home/home.module';
import { PasswordsModule } from './passwords/passwords.module';




@NgModule({
  declarations: [],
  imports: [
    CommonModule,
    LoginModule,
    HomeModule,
    PasswordsModule
  ]
})
export class PagesModule { }
