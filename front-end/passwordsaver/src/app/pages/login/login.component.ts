import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, FormControl, Validators } from '@angular/forms';
import { environment } from '../../environment/environment';
import { Login } from '../../shared/models/login.model';
import { AuthService } from '../../shared/services/auth.service';
import { MessageService } from 'primeng/api';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent implements OnInit {
  loginForm!: FormGroup;
  
  constructor(private formBuilder: FormBuilder,
    private authService: AuthService,
    private messageService: MessageService
  ) { }

  ngOnInit(): void {
    // Fetch all the forms we want to apply custom Bootstrap validation styles to
    //var forms = document.querySelectorAll('.needs-validation')

    // Loop over them and prevent submission
    /*Array.prototype.slice.call(forms)
      .forEach(function (form) {
        form.addEventListener('submit', function (event: any) {
          console.log("CALLED");
          if (!form.checkValidity()) {
            event.preventDefault()
            event.stopPropagation()
          }

          console.log("VALID 1");
          
          form.classList.add('was-validated')
        }, false)
      })*/

    this.loginForm = this.formBuilder.group({
      'username': new FormControl('', Validators.required),
      'password': new FormControl('', Validators.required)
    });
  }

  onSubmit(event: any) {
    event.preventDefault();
    event.stopPropagation();

    if(!this.loginForm.valid)
      return;

    this.messageService.clear();

    let login: Login = {
      username: this.loginForm.controls['username'].value,
      password: this.loginForm.controls['password'].value
    }

    this.authService.login(login).subscribe({
      next: (response) => {
        this.messageService.add({ key: 'loginToast',
          severity: response.success ? 'success' : 'error',
          summary: response.success ? 'Login Successful' : 'Login Error',
          detail: response.errorMessage
        });

        // TODO: redirect 
      },
      error: (err) => this.messageService.add({ key: 'loginToast',
          severity: 'error',
          summary: 'Login Error',
          detail: err.errorMessage
        })
    });
  }
}
