import { Component } from '@angular/core';
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms';
import {Api} from '../service/api';
import {Router, RouterLink} from '@angular/router';
import {CommonModule} from '@angular/common';

@Component({
  selector: 'app-login',
  imports: [ CommonModule,ReactiveFormsModule,RouterLink ],
  templateUrl: './login.html',
  styleUrl: './login.css',
})
export class Login {

  loginForm:FormGroup;
  error:string="";

  constructor(private fb:FormBuilder , private api:Api, private router:Router) {

    this.loginForm=this.fb.group({
      username :["", Validators.required],
      password :["" , Validators.required]
    })

  }

  onSubmit(){

    if(this.loginForm.invalid){
      this.error ="Please fill in all field";
      return;
    }

    this.error =""

    this.api.loginUser(this.loginForm.value).subscribe({
      next:(res:any) =>{

        if(res.statusCode === 200){
          this.api.saveToken(res.data)
          this.router.navigate(['/projects']);
        }else{
          this.error = res.message || "Login Not Succesfful"
        }
      },

      error:(error:any) =>{
        this.error = error.error?.message  || error.message
      }

    })
  }

}
