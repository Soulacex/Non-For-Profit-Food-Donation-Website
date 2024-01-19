import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { BehaviorSubject, Observable, of } from 'rxjs';
import { catchError, tap } from 'rxjs/operators';
import { Needs } from 'src/app/needs'
import { SupportedNeed } from './SupportedNeeds';

@Injectable({
  providedIn: 'root',
})
export class Cupboard {
  private cupboardUrl = 'http://localhost:8080/ufund';
  private cupboardNeedsSubject = new BehaviorSubject<Needs[]>([]);
  cupboardNeeds$ = this.cupboardNeedsSubject.asObservable();


  httpOptions = {
    headers: new HttpHeaders({ 'Content-Type': 'application/json' }),
  };

  constructor(private http: HttpClient) {}

  /**
   * Retrieves all needs from the cupboard.
   * @returns Observable array of Needs.
   */
  getAllcupboardNeeds(): Observable<Needs[]> {
    return this.http.get<Needs[]>(this.cupboardUrl).pipe(
      catchError(this.handleError<Needs[]>('getAllcupboard', []))
    );
  }

  /**
   * Adds a new need to the cupboard.
   * @param need The need to be added.
   * @returns Observable of the added Need.
   */
   createNeed(need: Needs): Observable<Needs> {
    return this.http.post<Needs>(this.cupboardUrl, need, this.httpOptions).pipe(
      tap((addedNeed: Needs) => {
        const currentNeeds = this.cupboardNeedsSubject.value.slice();
        currentNeeds.push(addedNeed);
        this.updateCupboardNeeds(currentNeeds);
        this.log(`added need with name=${need.name}`);
      }),
      catchError(this.handleError<Needs>('createNeed'))
    );
  }  

  /**
   * Updates an existing need in the cupboard.
   * @param need The updated need.
   * @returns Observable of the updated Need.
   */
  updateNeed(need: Needs): Observable<any> {
    const url = `${this.cupboardUrl}/${need.name}`;
    return this.http.put(url, need, this.httpOptions).pipe(
      tap((_) => this.log(`updated need with name=${need.name}`)),
      catchError(this.handleError<any>('updateNeed'))
    );
  }

  /**
   * Deletes a need from the cupboard.
   * @param name The name of the need to be deleted.
   * @returns Observable of the deleted Need.
   */
  deleteNeed(name: string): Observable<Needs> {
    const url = `${this.cupboardUrl}/${name}`;
    return this.http.delete<Needs>(url, this.httpOptions).pipe(
      tap((_) => this.log(`deleted need with name=${name}`)),
      catchError(this.handleError<Needs>('deleteNeed'))
    );
  }

  /**
   * Searches for needs in the cupboard based on a search term.
   * @param term The search term for filtering needs.
   * @returns Observable array of Needs that match the search term.
   */
  searchNeeds(term: string): Observable<Needs[]> {
    if (!term.trim()) {
      return of([]);
    }
    return this.http.get<Needs[]>(`${this.cupboardUrl}/search?name=${term}`).pipe(
      tap((x) => x.length ? this.log(`found needs matching "${term}"`) : this.log(`no needs matching "${term}"`)),
      catchError(this.handleError<Needs[]>('searchNeeds', []))
    );
  }

  getNeed(name: string): Observable<Needs> {
    const url = `${this.cupboardUrl}/${name}`;
    return this.http.get<Needs>(url).pipe(
      catchError(this.handleError<Needs>(`getNeed name=${name}`))
    );
  }  

  /**
   * Function that returns all the Needs within the Cupboard
   * @return Array of all the Needs within the Cupboard
   **/
   updateCupboardNeeds(needs: Needs[]): void {
    this.cupboardNeedsSubject.next(needs);
  }

  getCupboardNeeds(): Needs[] {
    return this.cupboardNeedsSubject.value;
  }

  getCupboardNeedsObservable(): Observable<Needs[]> {
    return this.cupboardNeeds$;
  }

  /**
   * Searches for needs in the cupboard based on a search term.
   * @param term The search term for filtering needs.
   * @returns Observable array of Needs that match the search term.
   */
   searchPartialName(searchText: string): Observable<Needs[]> {
    return this.http.get<Needs[]>(`${this.cupboardUrl}/?name=${searchText}`).pipe(
      tap((x) => x.length ? this.log(`found needs matching "${searchText}"`) : this.log(`no needs matching "${searchText}"`)),
      catchError(this.handleError<Needs[]>('searchPartialName', []))
    );
  }

  /**
   * Retrieves all needs from the cupboard.
   * @returns Observable array of Needs.
   */
  getAllNeeds(): Observable<Needs[]> {
    return this.http.get<Needs[]>(`${this.cupboardUrl}`).pipe(
      catchError(this.handleError<Needs[]>('getAllNeeds', []))
    );
  }

  addToBasket(need: Needs): void {
    const updatedNeeds = this.getCupboardNeeds().slice();
    const existingNeedIndex = updatedNeeds.findIndex((n) => n.name === need.name);
  
    if (existingNeedIndex !== -1 && updatedNeeds[existingNeedIndex].quantity > 0) {
      updatedNeeds[existingNeedIndex].quantity -= 1;
      this.updateCupboardNeeds(updatedNeeds);
    }
  }

  removeFromBasket(need: Needs): void {
    const updatedNeeds = this.getCupboardNeeds().slice();
    const existingNeedIndex = updatedNeeds.findIndex((n) => n.name === need.name);
  
    if (existingNeedIndex !== -1) {
      updatedNeeds[existingNeedIndex].quantity += 1;
      this.updateCupboardNeeds(updatedNeeds);
    }
  }

  /**
   * Handle Http operation that failed.
   * Let the app continue.
   * @param operation - name of the operation that failed
   * @param result - optional value to return as the observable result
   * @returns Observable of the result.
   */
  private handleError<T>(operation = 'operation', result?: T) {
    return (error: any): Observable<T> => {
      console.error(error);
      this.log(`${operation} failed: ${error.message}`);
      return of(result as T);
    };
  }

  /**
   * Log a CupboardService message to the console.
   * @param message - The message to be logged.
   */
  private log(message: string) {
    console.log(`CupboardService: ${message}`);
  }
}
