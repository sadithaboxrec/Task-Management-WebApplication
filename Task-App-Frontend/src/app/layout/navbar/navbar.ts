import { Component,HostListener  } from '@angular/core';
import {CommonModule} from '@angular/common';
import {Router, RouterLink} from '@angular/router';
import {Api} from '../../service/api';

@Component({
  selector: 'app-navbar',
  imports: [CommonModule,RouterLink],
  templateUrl: './navbar.html',
  styleUrl: './navbar.css',
})
export class Navbar {



  constructor(private readonly api:Api, private router:Router) {
  }

  get isAuthenticated():boolean{
    return this.api.isAuthenticated();
  }

  get username(): string {
    return this.api.getUsername() ?? 'User';
  }


  handleLogout():void{
    const islogout = window.confirm("Do  you want to logout?");

    if (islogout) {
      this.api.logout();
      this.router.navigate(['/login'])
    }

  }




}
