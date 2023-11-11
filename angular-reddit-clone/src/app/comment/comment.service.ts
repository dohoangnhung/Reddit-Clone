import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { CommentPayload } from './comment-payload';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class CommentService {

  constructor(private httpClient: HttpClient) { }

  postComment(commentPayload: CommentPayload): Observable<any> {
    return this.httpClient.post<CommentPayload>('http://localhost:8080/api/comments', commentPayload)
  }

  getAllCommentsForPost(postId: number): Observable<CommentPayload[]> {
    return this.httpClient.get<CommentPayload[]>('http://localhost:8080/api/comments/by-postId/' + postId)
  }

  getAllCommentsByUser(username: string): Observable<CommentPayload[]> {
    return this.httpClient.get<CommentPayload[]>('http://localhost:8080/api/comments/by-user/' + username)
  }
}
