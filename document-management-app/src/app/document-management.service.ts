import { DocumentSummary } from './documentSummary';
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { SearchRequest } from './searchRequest';
import { HttpHeaders } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class DocumentManagementService {

  private apiServiceUrlForDocumentSummaries = environment.allDocumentSummariesUrl;
  private searchServiceUrl=environment.searchDocumentSummariesUrl;

  constructor(private http: HttpClient) {}

  public getDocumentSummaries():Observable<DocumentSummary[]>{
    return this.http.get<DocumentSummary[]>(this.apiServiceUrlForDocumentSummaries);
  }

  public getSearchResults(searchRequest: SearchRequest): Observable<DocumentSummary[]>{
    const headers = new HttpHeaders().set('Content-Type', 'application/json');
    return this.http.post<DocumentSummary[]>(this.searchServiceUrl,searchRequest,{headers});

  }
}
