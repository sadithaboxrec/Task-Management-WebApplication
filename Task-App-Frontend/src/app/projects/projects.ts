import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { Api } from '../service/api';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-projects',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './projects.html',
  styleUrls: ['./projects.css']
})
export class Projects {
  // Array to store all projects
  projects: any[] = [];

  // Array to store filtered projects for display
  filteredProjects: any[] = [];

  // Variable to store error messages
  error: string = '';

  // Current priority filter setting
  priorityFilter: string = 'ALL';

  // Current completion status filter setting
  completionFilter: string = 'ALL';

  constructor(private api: Api, private router: Router) {
    // Redirect to login if user is not authenticated
    if (!this.api.isAuthenticated()) {
      this.router.navigate(['/login']);
      return;
    }

    // Fetch projects
    this.fetchProjects();
  }

  /**
   * Fetches all projects for the current user
   */
  // fetchProjects(): void {
  //   this.api.getAllProjects().subscribe({
  //     next: (res) => {
  //       if (res.statusCode === 200) {
  //         // Convert Id → id for Angular
  //         this.projects = res.data.map((p: any) => ({
  //           id: p.Id,
  //           title: p.name,
  //           description: p.description,
  //           priority: p.priority,
  //           completed: p.completed,
  //           dueDate: p.dueDate,
  //           createdAt: p.createdAt
  //         }));
  //         this.filteredProjects = [...this.projects];
  //       } else {
  //         this.error = res.message || 'Failed to fetch projects';
  //       }
  //     },
  //     error: (error) => {
  //       this.error = error.error?.message || error.message || 'Error fetching projects';
  //     }
  //   });
  // }

  // loading: boolean = true;
  //
  // fetchProjects(): void {
  //   this.loading = true;
  //   this.api.getAllProjects().subscribe({
  //     next: (res) => {
  //       if (res.statusCode === 200) {
  //         this.projects = res.data.map((p: any) => ({
  //           id: p.Id,
  //           title: p.name,
  //           description: p.description,
  //           priority: p.priority,
  //           completed: p.completed,
  //           dueDate: p.dueDate,
  //           createdAt: p.createdAt
  //         }));
  //         this.filteredProjects = [...this.projects];
  //       } else {
  //         this.error = res.message || 'Failed to fetch projects';
  //       }
  //       this.loading = false;
  //     },
  //     error: (err) => {
  //       this.error = err.error?.message || err.message || 'Error fetching projects';
  //       this.loading = false;
  //     }
  //   });
  // }


  // fetchProjects(): void {
  //   this.api.getAllProjects().subscribe(res => {
  //     id: p.Id
  //     this.projects = res.data;
  //     this.filteredProjects = [...this.projects];
  //   });
  // }


  fetchProjects(): void {
    this.api.getAllProjects().subscribe(res => {

      this.projects = res.data.map((p: any) => ({
        ...p,        // keep all backend fields
        id: p.Id     // 👈 normalize Id → id
      }));

      this.filteredProjects = [...this.projects];
    });
  }

  /**
   * Applies the current filters to the project list
   */
  // applyFilters(): void {
  //   let result = [...this.projects];
  //
  //   // Filter by completion status
  //   if (this.completionFilter !== 'ALL') {
  //     this.api.getProjectsByCompletionStatus(this.completionFilter === 'COMPLETED').subscribe({
  //       next: (res) => {
  //         if (res.statusCode === 200) {
  //           result = res.data;
  //
  //           if (this.priorityFilter !== 'ALL') {
  //             this.applyPriorityFilter(result);
  //           } else {
  //             this.filteredProjects = result;
  //           }
  //         }
  //       },
  //       error: (error) => {
  //         this.error = error.error?.message || error.message || 'Error applying completion filter';
  //       }
  //     });
  //   } else if (this.priorityFilter !== 'ALL') {
  //     // Only filter by priority
  //     this.applyPriorityFilter(result);
  //   } else {
  //     this.filteredProjects = result;
  //   }
  // }

  applyFilters(): void {
    this.filteredProjects = this.projects.filter(project => {

      const completionMatch =
        this.completionFilter === 'ALL' ||
        (this.completionFilter === 'COMPLETED' && project.completed) ||
        (this.completionFilter === 'PENDING' && !project.completed);

      const priorityMatch =
        this.priorityFilter === 'ALL' ||
        project.priority === this.priorityFilter;

      return completionMatch && priorityMatch;
    });
  }


  /**
   * Helper method to apply priority filter
   */
  private applyPriorityFilter(currentResult: any[]): void {
    this.api.getProjectsByPriority(this.priorityFilter).subscribe({
      next: (res) => {
        if (res.statusCode === 200) {
          if (this.completionFilter !== 'ALL') {
            const priorityProjects = res.data;
            currentResult = currentResult.filter(project =>
              priorityProjects.some((pp: any) => pp.id === project.id)
            );
          } else {
            currentResult = res.data;
          }
          this.filteredProjects = currentResult;
        }
      },
      error: (error) => {
        this.error = error.error?.message || error.message || 'Error applying priority filter';
      }
    });
  }

  /**
   * Toggles the completion status of a project
   */
  toggleComplete(project: any): void {
    this.api.updateProject({
      id: project.id,
      completed: !project.completed
    }).subscribe({
      next: (res) => {
        if (res.statusCode === 200) {
          this.projects = this.projects.map(p =>
            p.id === project.id ? { ...p, completed: !p.completed } : p
          );
          this.applyFilters();
        }
      },
      error: (error) => {
        this.error = error.error?.message || error.message || 'Error updating project';
      }
    });
  }

  /**
   * Resets all filters
   */
  resetFilters(): void {
    this.priorityFilter = 'ALL';
    this.completionFilter = 'ALL';
    this.applyFilters();
  }
}
