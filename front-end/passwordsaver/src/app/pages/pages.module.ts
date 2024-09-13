import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { LoginModule } from './login/login.module';
import { HomeModule } from './home/home.module';
import { PasswordsModule } from './passwords/passwords.module';
import { ServicesModule } from './services/services.module';




@NgModule({
  declarations: [],
  imports: [
    CommonModule,
    LoginModule,
    HomeModule,
    PasswordsModule,
    ServicesModule
  ]
})
export class PagesModule { }
