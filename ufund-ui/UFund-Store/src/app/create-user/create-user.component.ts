import { Component, OnInit } from '@angular/core';
import { AuthService } from '../auth.service';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { Router } from '@angular/router'
import { helperAccount } from '../helperAccount';

@Component({
  selector: 'app-create-user',
  templateUrl: './create-user.component.html',
  styleUrls: ['./create-user.component.scss']
})
export class CreateUserComponent implements OnInit{
  currentAccounts: String[] = []
  users: helperAccount[] = []
  form: FormGroup = this.fb.group({
  username: ['',Validators.required]
  });
  constructor(private authService: AuthService, private fb: FormBuilder, private router: Router){}
 
  ngOnInit(): void {
    this.authService.getAccounts().subscribe(users => this.currentAccounts = users)
  }
  
  createUser(username: string): void {
    username = username.trim();
    if (this.currentAccounts.includes(username)){
      alert("Creation Failed: User already exists")
    }
    else{
      this.authService.createUser({ username } as helperAccount).subscribe(helperAccount => {this.users.push(helperAccount)})
      alert("Creation Successful, return login to access page")
    }
    }
}

