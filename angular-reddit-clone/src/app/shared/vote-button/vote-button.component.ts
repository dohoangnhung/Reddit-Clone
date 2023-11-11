import { Component, Input } from '@angular/core';
import { PostModel } from '../post-model';
import { faArrowDown, faArrowUp } from '@fortawesome/free-solid-svg-icons';
import { VoteService } from '../vote.service';
import { VotePayload } from './vote-payload';
import { VoteType } from './vote-type';
import { throwError } from 'rxjs';
import { AuthService } from 'src/app/auth/shared/auth.service';
import { PostService } from '../post.service';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-vote-button',
  templateUrl: './vote-button.component.html',
  styleUrls: ['./vote-button.component.css']
})
export class VoteButtonComponent {
  @Input() post!: PostModel;
  votePayload!: VotePayload;

  faArrowUp = faArrowUp;
  faArrowDown = faArrowDown;
  upvoteColor!: string;
  downvoteColor!: string;

  constructor(private voteService: VoteService, private authService: AuthService, private postService: PostService, private toastr: ToastrService) {
    this.votePayload = {
      voteType: VoteType.UPVOTE,
      postId: 1
    }
  }

  ngOnInit(): void {
    this.updateVoteDetails();
  }

  upvotePost() {
    this.votePayload.voteType = VoteType.UPVOTE;
    this.vote();
  }

  downvotePost() {
    this.votePayload.voteType = VoteType.DOWNVOTE;
    this.vote();
  }

  private vote() {
    this.votePayload.postId = this.post.id;
    this.voteService.vote(this.votePayload).subscribe({
      next: () => {
        this.updateVoteDetails();
      },
      error: error => {
        this.toastr.error(error.error.message);
        throwError(() => error);
      }
    })
  }

  private updateVoteDetails() {
    this.postService.getPost(this.post.id).subscribe(post => {
      this.post = post;
    })
  }
}
