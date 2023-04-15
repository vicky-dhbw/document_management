import { DocumentMetadata } from './DocumentMetadata';
import { environment } from 'src/environments/environment';
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpHeaders } from '@angular/common/http';


@Injectable({
  providedIn: 'root'
})
export class PostMetadataAndFileService {
  private apiServerOfficeIndexingServiceUrl=environment.baseUrlOfficeIndexingService;
  private apiServerPdfIndexingServiceUrl=environment.baseUrlOfPdfIndexingService;
  private apiServerDocumentManagementService=environment.baseUrlOfDocumentManagementServer;


  constructor(private http: HttpClient) { }

  saveDocument(file: File, documentMetadata: DocumentMetadata): Observable<any> {
    const formData: FormData = new FormData();
    formData.append('file', file, file.name);
    formData.append('metadata', JSON.stringify(documentMetadata));
  
    const headers = new HttpHeaders();
    headers.append('Content-Type', 'multipart/form-data');
  
    return this.http.post(this.apiServerDocumentManagementService, formData, { headers });
  }

  
}
