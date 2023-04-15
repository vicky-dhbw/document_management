import { TestBed } from '@angular/core/testing';

import { PostMetadataAndFileService } from './post-metadata-and-file.service';

describe('PostMetadataAndFileService', () => {
  let service: PostMetadataAndFileService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PostMetadataAndFileService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
