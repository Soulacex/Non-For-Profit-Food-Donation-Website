import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, of } from 'rxjs';
import { catchError, tap } from 'rxjs/operators';
import { Needs } from './needs';
import { SupportedNeed } from './SupportedNeeds';

@Injectable({
  providedIn: 'root',
})
export class HelperService {
  private baseUrl = 'http://localhost:8080'; 
  private helperUrl = `${this.baseUrl}/helper`;

  httpOptions = {
    headers: new HttpHeaders({ 'Content-Type': 'application/json' }),
  };

  constructor(private http: HttpClient) {}

  addSupportedNeed(need: SupportedNeed): Observable<Needs> {
    return this.http.post<Needs>(`${this.helperUrl}/addneed`,need,this.httpOptions).pipe(
      tap(_ => {}),
      catchError(this.handleError<Needs>('addSupportedNeed'))
    );
  }  

  removeSupportedNeed(need: SupportedNeed): Observable<Needs> {
    let need_name = need.need_name;
    let httpOptions = {
      headers: new HttpHeaders({ 'Content-Type': 'application/json' }),
    };
    return this.http.delete<Needs>(`${this.helperUrl}/${need_name}`, httpOptions).pipe(
      tap(_ => catchError(this.handleError<Needs>('removeSupportedNeed')))
    );
  }
  

  checkOut(username: string): Observable<Needs> {
    return this.http.put<Needs>(`${this.helperUrl}/${username}`, this.httpOptions).pipe(
      tap(_ => catchError(this.handleError<any>('checkOut')))
    );
  }

  getSupportedNeeds(username: string): Observable<Map<String, SupportedNeed>> {
    return this.http.get<Map<String, SupportedNeed>>(`${this.helperUrl}/${username}`, this.httpOptions).pipe(
      tap(_ => catchError(this.handleError<Needs[]>('searchPartialName', [])))
    );
  }

  private handleError<T>(operation = 'operation', result?: T) {
    return (error: any): Observable<T> => {
      console.error(error);
      return of(result as T);
    };
  }
}