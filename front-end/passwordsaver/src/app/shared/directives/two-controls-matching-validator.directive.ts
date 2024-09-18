import { Directive } from '@angular/core';
import { AbstractControl, ValidatorFn } from '@angular/forms';

@Directive({
  selector: '[appTwoControlsMatchingValidator]'
})
export class TwoControlsMatchingValidatorDirective {

  constructor() { }

  // metodo statico che controlla se i due controlli hanno lo stesso valore
  static twoControlsMatchingValidator(controlName: string, matchingControlName: string, message: string): ValidatorFn {
    // ValidatorFn vuole come ritorno null se non ci sono errori, mentre se ci sono errori
    // vuole un oggetto errore della forma { nomeErrore: 'valore errore'}
    return (abstractControl: AbstractControl) => {
      // abstractControl è il formgroup, quindi da questo possiamo recuperarci i controlli
      // di cui ci vengono passati i parametri
      const control = abstractControl.get(controlName);
      const matchingControl = abstractControl.get(matchingControlName);

      if(control == null || matchingControl == null)
        return null;

      // controlliamo se uno dei formcontrol (o entrambi) ha già altri errori,
      // in tal caso non rifacciamo la validazione, lasciamo che prima
      // vengano validati gli altri controlli, poi validiamo questo
      if((control!.errors && !control!.errors?.['twoControlsMatchingValidator']) ||
            (matchingControl!.errors && !matchingControl!.errors?.['twoControlsMatchingValidator']))
        return null;

      // controlliamo se i valori dei due controlli sono uguali
      if(control!.value !== matchingControl!.value) {
        // se sono diversi settiamo l'errore
        const error = {twoControlsMatchingValidator: message};
        // setErrors aggiunge al formcontrol la classe ng-invalid
        control!.setErrors(error);
        matchingControl!.setErrors(error);
        return error;
      } else {
        // rimuoviamo l'errore (toglie la classe ng-invalid)
        control!.setErrors(null);
        matchingControl!.setErrors(null);
        return null;
      }
    }
  }
}
