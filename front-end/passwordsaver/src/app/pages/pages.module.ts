import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { LoginModule } from './login/login.module';
import { HomeModule } from './home/home.module';
import { PasswordsModule } from './passwords/passwords.module';
import { ServicesModule } from './services/services.module';
import { ServicetypesModule } from './servicetypes/servicetypes.module';
import { UsersModule } from './users/users.module';
import { UsertypesModule } from './usertypes/usertypes.module';
import { SignupModule } from './signup/signup.module';




@NgModule({
  declarations: [],
  imports: [
    CommonModule,
    LoginModule,
    HomeModule,
    PasswordsModule,
    ServicesModule,
    ServicetypesModule,
    UsersModule,
    UsertypesModule,
    SignupModule
  ]
})
export class PagesModule { }
