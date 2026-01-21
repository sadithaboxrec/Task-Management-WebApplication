import { Component } from '@angular/core';
import {CommonModule} from '@angular/common';
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms';
import {Router, RouterLink} from '@angular/router';
import {Api} from '../service/api';


@Component({
  selector: 'app-register',
  imports: [CommonModule , ReactiveFormsModule,RouterLink],
  templateUrl: './register.html',
  styleUrl: './register.css',
})
export class Register {

  registrationForm:FormGroup;
  error:string="";

  constructor(private fb:FormBuilder , private api:Api, private router:Router) {

    this.registrationForm=this.fb.group({
      username :["", Validators.required],
      password :["" , Validators.required]
    })

  }

  onSubmit(){

    if(this.registrationForm.invalid){
      this.error ="Please fill in all field";
      return;
    }

    this.error =""

    this.api.registerUser(this.registrationForm.value).subscribe({
      next:(res:any) =>{

        if(res.statusCode === 200){
          this.router.navigate(['/login']);
        }else{
          this.error = res.message || "Registartion Not Succesfful"
        }
      },

      error:(error:any) =>{
        this.error = error.error?.message  || error.message
      }

    })
  }

}
