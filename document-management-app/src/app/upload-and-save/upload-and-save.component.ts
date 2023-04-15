import { DownloadFileService } from './../download-file.service';
import { DocumentMetadata } from './../DocumentMetadata';
import { MetadataService } from './../metadata-service.service';
import { NgClass } from '@angular/common';
import { Component, OnInit, ViewChild, ElementRef } from '@angular/core';
import { Meta } from '@angular/platform-browser';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { NgForm } from '@angular/forms';
import { PostMetadataAndFileService } from '../post-metadata-and-file.service';
import { UploadResponse } from './UploadResponse';
import { saveAs } from 'file-saver';
import { DocumentSummary } from '../documentSummary';
import { DocumentManagementService } from '../document-management.service';
import * as bootstrap from 'bootstrap';
import { SearchRequest } from '../searchRequest';

@Component({
  selector: 'app-upload-and-save',
  templateUrl: './upload-and-save.component.html',
  styleUrls: ['./upload-and-save.component.css'],
})
export class UploadAndSaveComponent {
  public answer: string = '';
  public metadata = new DocumentMetadata();
  saveButtonIsDisabled: boolean = true;
  public extension: string = '';
  showFileExtenisonErrorAlert: boolean = false;
  showServerError: boolean = false;
  uploadedFile?: File;
  fileSizeLimitExceeded: boolean = false;
  public tagString: string = '';
  public tags: string[] = [];
  uploadSuccess: boolean = false;
  uploadResponsefilename: string = '';
  uploadResponseFilesize: number = 0;
  uploadError: boolean = false;
  public documentSummaries: DocumentSummary[] = [];
  @ViewChild('downloadModal') downloadModal!: ElementRef;
  public searchedKeyword: string = '';
  public textOrTag: string = 'text';
  public searchedDocumentSummaries: DocumentSummary[] = [];
  noContents: boolean = false;
  noSearchResults: boolean = false;

  constructor(
    private metadataService: MetadataService,
    private postMetadataAndFileService: PostMetadataAndFileService,
    private DownloadFileService: DownloadFileService,
    private documentManagementService: DocumentManagementService
  ) {}
  ngOnInit(): void {
    this.metadataService.getMetadata().subscribe((response: any) => {
      console.log(response);
      this.answer = response.message;
    });

    this.getDocumentSummaries();
  }

  public onSubmit(event: Event) {
    this.showServerError = false;
    this.uploadSuccess = false;
    console.log('button clicked');
    event.preventDefault();
    const fileInput = event.target as HTMLFormElement;
    const fileInputElement = fileInput[0] as HTMLInputElement | null;

    if (fileInputElement && fileInputElement.files) {
      const file: File = fileInputElement.files[0];
      this.uploadedFile = file;
      this.extension = file.name.split('.').pop()?.toLowerCase() ?? '';
      console.log(this.extension);

      if (this.extension === 'pdf' || this.extension === 'docx') {
        this.showFileExtenisonErrorAlert = false;
        this.fileSizeLimitExceeded = false;
        this.metadataService.getFileMetadata(file).subscribe(
          (response: any) => {
            this.metadata.settitle(response.title);
            this.metadata.setfilename(response.filename);
            this.metadata.setcreator(response.creator);
            this.metadata.setdateOfCreation(response.dateOfCreation);
            this.metadata.setfilesize(response.filesize);
            this.metadata.settags([]);

            console.log(this.metadata?.creator);
            console.log(this.metadata?.filename);
            this.uploadedFile = file;
            this.saveButtonIsDisabled = false;
          },
          (error: any) => {
            console.log('An error occurred:', error);
            if (error.status === 500 || 30566) {
              this.restartMetaDataApp();
              this.showServerError = true;
              if (error.status === 30566) {
                this.fileSizeLimitExceeded = true;
              }
            }
          }
        );
      } else {
        this.showFileExtenisonErrorAlert = true;
      }
    }
  }

  public onClick() {
    console.log('Button was clicked');
  }

  public onUpload(uploadForm: NgForm): void {
    console.log(this.metadata.title);
    console.log(this.metadata.creator);
    console.log(this.metadata.filename);
    console.log(this.metadata.filesize);
    console.log(this.metadata.dateOfCreation);

    this.tagString = uploadForm.value.tags;
    this.tags = this.tagString.split(';');

    this.metadata.settags(this.tags);
    console.log(this.tagString);

    if (this.uploadedFile) {
      this.postMetadataAndFileService
        .saveDocument(this.uploadedFile, this.metadata)
        .subscribe(
          (response: UploadResponse) => {
            this.uploadResponsefilename = response.filename;
            this.uploadResponseFilesize = response.filesize;

            if (
              this.uploadResponsefilename === 'error' &&
              this.uploadResponseFilesize === 0
            ) {
              this.uploadError = true;
              setTimeout(() => {
                this.uploadError = false;
              }, 5000);
            } else {
              this.uploadSuccess = true;
              setTimeout(() => {
                this.uploadSuccess = false;
              }, 5000);
            }
            this.noContents=false;
            this.getDocumentSummaries();
          },
          (error: any) => {
            console.error('Error:', error);
            this.uploadError = true;
            setTimeout(() => {
              this.uploadError = false;
            }, 5000);
          }
        );
    } else {
      console.log('No file selected to upload');
    }
    uploadForm.reset();
  }

  private restartMetaDataApp() {
    this.metadataService.restartMetadataServer().subscribe(
      (response: any) => {
        console.log('Server response:', response);
      },
      (error: any) => {
        console.error('Error:', error);
      }
    );
  }

  downloadFile(filename: string) {
    this.DownloadFileService.downloadFile(filename).subscribe((blob) =>
      saveAs(blob, filename)
    );
  }

  public getDocumentSummaries(): void {
    this.documentManagementService.getDocumentSummaries().subscribe(
      (response: DocumentSummary[]) => {
        this.documentSummaries = response;
        if (this.documentSummaries.length === 0) {
          this.noContents = true;
        }
      },
      (error: HttpErrorResponse) => {
        alert(error.message);
      }
    );
  }

  public onSearch() {
    const modal = document.getElementById('downloadModal');
    this.noSearchResults = false;

    const searchRequest: SearchRequest = {
      searchKeyword: this.searchedKeyword,
      textOrTag: this.textOrTag,
    };
    this.documentManagementService.getSearchResults(searchRequest).subscribe(
      (documentSummaryList) => {
        this.searchedDocumentSummaries = documentSummaryList;
        if (this.searchedDocumentSummaries.length === 0) {
          this.noSearchResults = true;
        }
      },
      (error) => console.error(error)
    );

    console.log('Searched keyword:', this.searchedKeyword); // Add this line
    if (this.searchedKeyword !== '') {
      if (modal) {
        const modalInstance = new bootstrap.Modal(modal);
        modalInstance.show();
      }
    }
  }
}
