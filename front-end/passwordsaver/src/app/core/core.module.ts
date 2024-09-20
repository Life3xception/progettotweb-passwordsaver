import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HeaderComponent } from './components/header/header.component';
import { UsertypeFormComponent } from './components/usertype-form/usertype-form.component';
import { UserFormComponent } from './components/user-form/user-form.component';
import { ReactiveFormsModule } from '@angular/forms';
import { ToastModule } from 'primeng/toast';



@NgModule({
  declarations: [
    HeaderComponent,
    UsertypeFormComponent,
    UserFormComponent
  ],
  imports: [
    CommonModule,
    ReactiveFormsModule,
    ToastModule
  ],
  exports: [
    HeaderComponent,
    UserFormComponent,
    UsertypeFormComponent
  ]
})
export class CoreModule { }
