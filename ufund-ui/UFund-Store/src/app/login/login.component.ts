import { Component, OnInit } from '@angular/core';
import { AuthService } from '../auth.service';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { Router } from '@angular/router'
import { UserAccount } from '../UserAccount';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit{
  currentAccounts: String[] = []

  static currentUser = ""
  
  form: FormGroup = this.fb.group({
    username: ['',Validators.required]
  });
  constructor(private authService: AuthService, private fb: FormBuilder, private router: Router){}
  ngOnInit(): void {
      this.authService.getAccounts().subscribe(users => this.currentAccounts = users)
  }
  
  login(loginVal: string){
    let validName = ""
    this.currentAccounts.forEach(UserAccount => {
      if (UserAccount===loginVal){
        validName = UserAccount.toString()
      }
    });
    if (validName === ""){
      alert("Invalid Login")
    } else if (validName === "admin"){
      this.router.navigateByUrl('/admin');
    } else {
      LoginComponent.currentUser = loginVal;
      this.router.navigateByUrl('/helper');
    }
    };
}
