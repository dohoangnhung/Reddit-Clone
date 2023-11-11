import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { SubredditModel } from './subreddit-response';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class SubredditService {

  constructor(private httpClient: HttpClient) { }

  getAllSubreddits(): Observable<Array<SubredditModel>> {
    return this.httpClient.get<Array<SubredditModel>>('http://localhost:8080/api/subreddit')
  }

  createSubreddit(subredditModel: SubredditModel): Observable<SubredditModel> {
    return this.httpClient.post<SubredditModel>('http://localhost:8080/api/subreddit', subredditModel);
  }
}
