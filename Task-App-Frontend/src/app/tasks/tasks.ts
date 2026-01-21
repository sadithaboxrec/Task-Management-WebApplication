import { Component } from '@angular/core';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Api } from '../service/api';

@Component({
  selector: 'app-tasks',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './tasks.html',
})
export class Tasks {
  projectId!: number;

  tasks: any[] = [];
  filteredTasks: any[] = [];
  error: string = '';

  priorityFilter: string = 'ALL';
  completionFilter: string = 'ALL';

  // constructor(
  //   private api: Api,
  //   private route: ActivatedRoute,
  //   private router: Router
  // ) {
  //   // Get projectId from route params and fetch tasks
  //   this.route.params.subscribe(params => {
  //     this.projectId = +params['projectId']; // or parseInt(params['projectId'], 10)
  //     console.log('Project ID:', this.projectId);
  //     this.fetchTasks();
  //   });
  // }

  constructor(
    private api: Api,
    private route: ActivatedRoute,
    private router: Router
  ) {
    this.route.params.subscribe(params => {
      const id = Number(params['projectId']);

      if (!id || isNaN(id)) {
        this.error = 'Invalid project ID';
        return;
      }

      this.projectId = id;
      this.fetchTasks();
    });
  }


  fetchTasks(): void {
    this.api.getTasksByProject(this.projectId).subscribe({
      next: res => {
        if (res.statusCode === 200) {
          this.tasks = res.data;
          this.filteredTasks = res.data;
        } else {
          this.error = res.message || 'Failed to fetch tasks';
        }
      },
      error: error => {
        this.error = error.error?.message || error.message || 'Error fetching tasks';
      }
    });
  }

  // applyFilters(): void {
  //   let result = [...this.tasks];
  //
  //   // Filter by completion status first
  //   if (this.completionFilter !== 'ALL') {
  //     this.api.getTasksByCompletionStatus(this.projectId, this.completionFilter === 'COMPLETED')
  //       .subscribe({
  //         next: res => {
  //           if (res.statusCode === 200) {
  //             result = res.data;
  //             if (this.priorityFilter !== 'ALL') this.applyPriorityFilter(result);
  //             else this.filteredTasks = result;
  //           }
  //         },
  //         error: error => {
  //           this.error = error.error?.message || error.message || 'Error applying completion filter';
  //         }
  //       });
  //   } else if (this.priorityFilter !== 'ALL') {
  //     this.applyPriorityFilter(result);
  //   } else {
  //     this.filteredTasks = result;
  //   }
  // }


  applyFilters(): void {
    this.filteredTasks = this.tasks.filter(task => {

      const completionMatch =
        this.completionFilter === 'ALL' ||
        (this.completionFilter === 'COMPLETED' && task.completed) ||
        (this.completionFilter === 'PENDING' && !task.completed);

      const priorityMatch =
        this.priorityFilter === 'ALL' ||
        task.priority === this.priorityFilter;

      return completionMatch && priorityMatch;
    });
  }


  private applyPriorityFilter(currentResult: any[]): void {
    this.api.getTasksByPriority(this.projectId, this.priorityFilter).subscribe({
      next: res => {
        if (res.statusCode === 200) {
          if (this.completionFilter !== 'ALL') {
            const priorityTasks = res.data;
            currentResult = currentResult.filter(task =>
              priorityTasks.some((pt: any) => pt.id === task.id)
            );
          } else {
            currentResult = res.data;
          }
          this.filteredTasks = currentResult;
        }
      },
      error: error => {
        this.error = error.error?.message || error.message || 'Error applying priority filter';
      }
    });
  }

  toggleComplete(task: any): void {
    this.api.updateTask({ id: task.id, completed: !task.completed }).subscribe({
      next: res => {
        if (res.statusCode === 200) {
          this.tasks = this.tasks.map(t => t.id === task.id ? { ...t, completed: !t.completed } : t);
          this.applyFilters();
        }
      },
      error: error => {
        this.error = error.error?.message || error.message || 'Error updating task';
      }
    });
  }

  resetFilters(): void {
    this.priorityFilter = 'ALL';
    this.completionFilter = 'ALL';
    this.applyFilters();
  }
}
