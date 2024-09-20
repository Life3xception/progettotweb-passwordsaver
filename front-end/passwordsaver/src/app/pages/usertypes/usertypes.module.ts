import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { UsertypesRoutingModule } from './usertypes-routing.module';
import { UsertypesComponent } from './usertypes.component';
import { NewUsertypeComponent } from './new-usertype/new-usertype.component';
import { ModifyUsertypeComponent } from './modify-usertype/modify-usertype.component';
import { ToastModule } from 'primeng/toast';
import { ReactiveFormsModule } from '@angular/forms';
import { MatIconModule } from '@angular/material/icon';
import { CoreModule } from '../../core/core.module';



@NgModule({
  declarations: [
    UsertypesComponent,
    NewUsertypeComponent,
    ModifyUsertypeComponent
  ],
  imports: [
    CommonModule,
    ToastModule,
    ReactiveFormsModule,
    MatIconModule,
    UsertypesRoutingModule,
    CoreModule
  ]
})
export class UsertypesModule { }
