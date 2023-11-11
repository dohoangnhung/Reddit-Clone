import { Component } from '@angular/core';
import { SubredditModel } from '../subreddit-response';
import { SubredditService } from '../subreddit.service';
import { throwError } from 'rxjs';

@Component({
  selector: 'app-list-subreddits',
  templateUrl: './list-subreddits.component.html',
  styleUrls: ['./list-subreddits.component.css']
})
export class ListSubredditsComponent {
  subreddits!: Array<SubredditModel>;

  constructor(private subredditService: SubredditService) {}

  ngOnInit() {
    this.subredditService.getAllSubreddits().subscribe({
      next: data => {
        this.subreddits = data;
      },
      error: error => {
        throwError(() => error);
      }
    })
  }
}
