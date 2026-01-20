import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Observable} from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class Api {

  private API_URL="http://localhost:8080/api/v1"

  constructor(private http:HttpClient) {

  }

  saveToken(token:string):void{
    localStorage.setItem("token",token);
  }

  getToken():string |null{
    return localStorage.getItem("token");
  }

  isAuthenticated():boolean{
    return !!localStorage.getItem("token");
  }

  logout():void{
     localStorage.removeItem("token");
  }

  private getHeader():HttpHeaders{
    const token=this.getToken();
    return new HttpHeaders({
      Authorization:`Bearer ${token}`,
      "Content-Type":"application/json"
    });
  }

                // Register

  registerUser(body:any):Observable<any>{
    return this.http.post(`${this.API_URL}/auth/register`,body);
  }

  loginUser(body:any):Observable<any>{
    return this.http.post(`${this.API_URL}/auth/login`,body);
  }

                  // Projects

  createProject(body: any): Observable<any> {
    return this.http.post(`${this.API_URL}/projects`, body, {
      headers: this.getHeader()
    });
  }

  updateProject(body: any): Observable<any> {
    return this.http.put(`${this.API_URL}/projects`, body, {
      headers: this.getHeader()
    });
  }

  getAllProjects(): Observable<any> {
    return this.http.get(`${this.API_URL}/projects`, {
      headers: this.getHeader()
    });
  }

  getProjectById(projectId: number): Observable<any> {
    return this.http.get(`${this.API_URL}/projects/${projectId}`, {
      headers: this.getHeader()
    });
  }

  deleteProject(projectId: number): Observable<any> {
    return this.http.delete(`${this.API_URL}/projects/${projectId}`, {
      headers: this.getHeader()
    });
  }

  getProjectsByCompletionStatus(completed: boolean): Observable<any> {
    return this.http.get(`${this.API_URL}/projects/status`, {
      headers: this.getHeader(),
      params: {
        completed: completed
      }
    });
  }

  getProjectsByPriority(priority: string): Observable<any> {
    return this.http.get(`${this.API_URL}/projects/priority`, {
      headers: this.getHeader(),
      params: {
        priority: priority
      }
    });
  }



  // Tasks

  createTask(projectId: number, body: any): Observable<any> {
    return this.http.post(
      `${this.API_URL}/projects/${projectId}/tasks`,
      body,
      { headers: this.getHeader() }
    );
  }


  getTasksByProject(projectId: number): Observable<any> {
    return this.http.get(
      `${this.API_URL}/projects/${projectId}/tasks`,
      { headers: this.getHeader() }
    );
  }


  updateTask(body: any): Observable<any> {
    return this.http.put(
      `${this.API_URL}/tasks`,
      body,
      { headers: this.getHeader() }
    );
  }

  deleteTask(taskId: number): Observable<any> {
    return this.http.delete(
      `${this.API_URL}/tasks/${taskId}`,
      { headers: this.getHeader() }
    );
  }


  getTasksByCompletionStatus(
    projectId: number,
    completed: boolean
  ): Observable<any> {
    return this.http.get(
      `${this.API_URL}/projects/${projectId}/tasks/status`,
      {
        headers: this.getHeader(),
        params: { completed }
      }
    );
  }


  getTasksByPriority(
    projectId: number,
    priority: string
  ): Observable<any> {
    return this.http.get(
      `${this.API_URL}/projects/${projectId}/tasks/priority`,
      {
        headers: this.getHeader(),
        params: { priority }
      }
    );
  }



}
