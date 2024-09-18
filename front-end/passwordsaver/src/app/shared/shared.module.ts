import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TwoControlsMatchingValidatorDirective } from './directives/two-controls-matching-validator.directive';



@NgModule({
  declarations: [
    TwoControlsMatchingValidatorDirective
  ],
  imports: [
    CommonModule
  ]
})
export class SharedModule { }
