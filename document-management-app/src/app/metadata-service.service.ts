import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';
import { HttpClient } from '@angular/common/http';
import { DocumentMetadata } from './DocumentMetadata';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class MetadataService {
  private apiServiceUrl = environment.apiBaseUrl;

  constructor(private http: HttpClient) {}

  public getMetadata(): Observable<string> {
    return this.http.get<any>(
      `${this.apiServiceUrl}/api/metadata-service/testing`
    );
  }

  public getFileMetadata(file: File): Observable<DocumentMetadata> {
    const formData = new FormData();
    formData.append('file', file, file.name);
    return this.http.post<DocumentMetadata>(
      `${this.apiServiceUrl}/api/metadata-service/getMetadata`,
      formData
    );
  }

  public restartMetadataServer(): any {
    console.log('restartMetadataServer() called');
    return this.http.post<any>(
      `${environment.restartMetadataServiceUrl}`,
      null
    );
  }
}
