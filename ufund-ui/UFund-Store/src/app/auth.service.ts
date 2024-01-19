import { Injectable, OnInit } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http'

import { Observable, of } from 'rxjs';
import { catchError, map, tap } from 'rxjs/operators';

import { helperAccount } from './helperAccount';
import { UserAccount } from './UserAccount';

@Injectable({
  providedIn: 'root'
})
export class AuthService{

  private accountsUrl = 'http://localhost:8080/account';  // URL to web api

  httpOptions = {
    headers: new HttpHeaders({ 'Content-Type': 'application/json' })
  };
  

  constructor(
    private http: HttpClient
    ) { }


  getAccounts(): Observable<String[]> {
    return this.http.get<String[]>(this.accountsUrl)
      .pipe(
        tap(_ =>catchError(this.handleError<String[]>('getAccounts', [])
      )));
  }

  createUser(account: helperAccount): Observable<helperAccount> {
    return this.http.post<helperAccount>(this.accountsUrl, account, this.httpOptions).pipe(
      tap((newAccount: helperAccount) =>
      catchError(this.handleError<string>('createUser'))
    ));
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
