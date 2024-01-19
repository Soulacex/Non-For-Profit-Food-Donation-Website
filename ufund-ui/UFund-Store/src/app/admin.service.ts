import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http'

import { Observable, of } from 'rxjs';
import { catchError, map, tap } from 'rxjs/operators';

import { Needs } from './needs';

@Injectable({
  providedIn: 'root'
})
export class AdminService {
  private adminUrl = 'http://localhost:8080/admin';  // URL to web api
  
  httpOptions = {
    headers: new HttpHeaders({ 'Content-Type': 'application/json' })
  };
  constructor(
    private http: HttpClient
  ) { }

  getNeeds(): Observable<Needs[]> {
    return this.http.get<Needs[]>(this.adminUrl).pipe(
      tap(_ => console.log('fetched needs')), 
      catchError(this.handleError<Needs[]>('getNeeds', []))
    );
  }  

  addNeed(need: Needs): Observable<Needs> {
    return this.http.post<Needs>(this.adminUrl, need, this.httpOptions).pipe(
      tap((newNeed: Needs) => 
      catchError(this.handleError<Needs>('addNeed')))
    );
  }

  deleteNeed(needName: string): Observable<Needs>{
    const url = `${this.adminUrl}/${needName}`;

    return this.http.delete<Needs>(url, this.httpOptions).pipe(
      tap(_ =>
      catchError(this.handleError<Needs>('deleteNeed')))
    );
  }

  updateNeed(need: Needs): Observable<any> {
    return this.http.put(this.adminUrl, need, this.httpOptions).pipe(
      tap(_ =>
      catchError(this.handleError<any>('updateNeed')))
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
