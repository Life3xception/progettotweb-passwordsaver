import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ServicetypesRoutingModule } from './servicetypes-routing.module';
import { ServicetypesComponent } from './servicetypes.component';
import { ToastModule } from 'primeng/toast';
import { ReactiveFormsModule } from '@angular/forms';
import { MatIconModule } from '@angular/material/icon';
import { NewServicetypeComponent } from './new-servicetype/new-servicetype.component';
import { ModifyServicetypeComponent } from './modify-servicetype/modify-servicetype.component';



@NgModule({
  declarations: [
    ServicetypesComponent,
    NewServicetypeComponent,
    ModifyServicetypeComponent
  ],
  imports: [
    CommonModule,
    ToastModule,
    ReactiveFormsModule,
    MatIconModule,
    ServicetypesRoutingModule
  ]
})
export class ServicetypesModule { }
