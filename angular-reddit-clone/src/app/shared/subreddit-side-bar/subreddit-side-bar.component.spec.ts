import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SubredditSideBarComponent } from './subreddit-side-bar.component';

describe('SubredditSideBarComponent', () => {
  let component: SubredditSideBarComponent;
  let fixture: ComponentFixture<SubredditSideBarComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [SubredditSideBarComponent]
    });
    fixture = TestBed.createComponent(SubredditSideBarComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
