import { Routes } from '@angular/router';
import {Register} from './register/register';
import {Login} from './login/login';
import {Projects} from './projects/projects';
import {Tasks} from './tasks/tasks';
import {Guard} from './service/guard';

export const routes: Routes = [

  {path:'register' , component:Register},
  {path:'login', component:Login},
  {path :'projects', component:Projects},
  { path: 'projects/:projectId/tasks', component: Tasks },
  {path:'tasks' ,component:Tasks}
];
