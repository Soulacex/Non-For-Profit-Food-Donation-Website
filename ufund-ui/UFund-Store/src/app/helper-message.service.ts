import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http'

import { Observable, of } from 'rxjs';
import { catchError, map, tap } from 'rxjs/operators';

import { HelperMessage } from './HelperMessage';

@Injectable({
  providedIn: 'root'
})
export class HelperMessageService {

  private messageUrl = 'http://localhost:8080/message';  // URL to web api

  httpOptions = {
    headers: new HttpHeaders({ 'Content-Type': 'application/json' })
  };

  constructor( private http: HttpClient) { }

  addMessage(helperMessage: HelperMessage): Observable<HelperMessage>{
    return this.http.post<HelperMessage>(this.messageUrl, helperMessage, this.httpOptions).pipe(
      tap((newMessage: HelperMessage) => 
      catchError(this.handleError<HelperMessage>('addMessage')))
    );
  }
  

  searchMessageId(Id: number): Observable<HelperMessage>{
    const url = `${this.messageUrl}/${Id}`;
    return this.http.get<HelperMessage>(url).pipe(
      tap(_=>catchError(this.handleError<HelperMessage>(`getMessage id=${Id}`)))
    );
  }

  getAllMessages(): Observable<HelperMessage[]>{
    return this.http.get<HelperMessage[]>(this.messageUrl).pipe(
      tap(_=>catchError(this.handleError<HelperMessage[]>(`getMessages`)))
    );
  }
  

  private handleError<T>(operation = 'operation', result?: T) {
    return (error: any): Observable<T> => {

      //TODO: send the error to remote logging infrastructure
      console.error(error); // log to console instead

      //Let the app keep running by returning an empty result.
      return of(result as T);
    }
  }
}
