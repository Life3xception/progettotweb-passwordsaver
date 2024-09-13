import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ServicesRoutingModule } from './services-routing.module';
import { ServicesComponent } from './services.component';
import { ToastModule } from 'primeng/toast';
import { MatIconModule } from '@angular/material/icon';
import { NewServiceComponent } from './new-service/new-service.component';
import { ModifyServiceComponent } from './modify-service/modify-service.component';
import { ReactiveFormsModule } from '@angular/forms';



@NgModule({
  declarations: [
    ServicesComponent,
    NewServiceComponent,
    ModifyServiceComponent
  ],
  imports: [
    CommonModule,
    ServicesRoutingModule,
    ToastModule,
    MatIconModule,
    ReactiveFormsModule
  ]
})
export class ServicesModule { }
