import { ComponentFixture, TestBed } from '@angular/core/testing';
import { FormsModule } from '@angular/forms';

import { UploadAndSaveComponent } from './upload-and-save.component';

describe('UploadAndSaveComponent', () => {
  let component: UploadAndSaveComponent;
  let fixture: ComponentFixture<UploadAndSaveComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ UploadAndSaveComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(UploadAndSaveComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
