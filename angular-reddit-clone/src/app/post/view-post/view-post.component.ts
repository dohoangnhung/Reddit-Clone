import { Component } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { throwError } from 'rxjs';
import { CommentPayload } from 'src/app/comment/comment-payload';
import { CommentService } from 'src/app/comment/comment.service';
import { PostModel } from 'src/app/shared/post-model';
import { PostService } from 'src/app/shared/post.service';

@Component({
  selector: 'app-view-post',
  templateUrl: './view-post.component.html',
  styleUrls: ['./view-post.component.css']
})
export class ViewPostComponent {
  postId!: number;
  post!: PostModel;

  commentForm!: FormGroup;
  commentPayload!: CommentPayload;
  comments!: CommentPayload[];

  constructor(private activateRoute: ActivatedRoute, private postService: PostService, private commentService: CommentService) {
    this.postId = this.activateRoute.snapshot.params['id'];
    this.postService.getPost(this.postId).subscribe({
      next: data => {
        this.post = data;
      },
      error: error => {
        throwError(() => error);
      }
    });

    this.commentForm = new FormGroup({
      text: new FormControl('', Validators.required)
    });
    this.commentPayload ={
      text: '',
      postId: this.postId
    }
  }

  ngOnInit(): void {
    this.getPostById();
    this.getCommentsForPost();
  }

  postComment() {
    this.commentPayload.text = this.commentForm.get('text')?.value;
    this.commentService.postComment(this.commentPayload).subscribe({
      next: () => {
        this.commentForm.get('text')?.setValue('');
        this.getCommentsForPost();
      },
      error: error => {
        throwError(() => error);
      }
    })
  }

  getPostById() {
    this.postService.getPost(this.postId).subscribe({
      next: data => {
        this.post = data;
      },
      error: error => {
        throwError(() => error);
      }
    })
  }

  getCommentsForPost() {
    this.commentService.getAllCommentsForPost(this.postId).subscribe({
      next: data => {
        this.comments = data;
      },
      error: error => {
        throwError(() => error);
      }
    })
  }
}
