import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class DownloadFileService {

 private downloadFileUrl=environment.downloadFileUrl;

  constructor(private http: HttpClient) { };


 public downloadFile(filePath: string): Observable<Blob> {
   return this.http.get(`${this.downloadFileUrl}/${filePath}`,
   { responseType: 'blob' });
 }

}
