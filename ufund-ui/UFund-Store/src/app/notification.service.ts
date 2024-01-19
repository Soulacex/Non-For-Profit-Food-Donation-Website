import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, of } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { Notification } from './notification';

@Injectable({
  providedIn: 'root'
})
export class NotificationService {
  private baseUrl: string = 'http://localhost:8080'; 

  constructor(private http: HttpClient) { }

  /**
   * Create a new notification.
   * @param notification The notification to be created.
   * @returns Observable<Notification> representing the created notification.
   */
  createNotification(notification: Notification): Observable<Notification> {
    const url = `${this.baseUrl}/notification`;
    return this.http.post<Notification>(url, notification)
      .pipe(
        catchError(this.handleError<Notification>('createNotification', notification))
      );
  }

  /**
   * Get a notification by its ID.
   * @param id The ID of the notification to retrieve.
   * @returns Observable<Notification> representing the retrieved notification.
   */
  getNotificationById(id: number): Observable<Notification> {
    const url = `${this.baseUrl}/notification/${id}`;
    return this.http.get<Notification>(url)
      .pipe(
        catchError(this.handleError<Notification>('getNotificationById'))
      );
  }

  /**
   * Get all notifications.
   * @returns Observable<Notification[]> representing an array of all notifications.
   */
  getAllNotifications(): Observable<Notification[]> {
    const url = `${this.baseUrl}/notification`;
    return this.http.get<Notification[]>(url)
      .pipe(
        catchError(this.handleError<Notification[]>('getAllNotifications', []))
      );
  }

  /**
   * Handle HTTP errors.
   * @param operation - Name of the operation that failed.
   * @param result - Optional value to return as the observable result.
   * @returns A function that handles HTTP errors and returns an empty result.
   */
  private handleError<T>(operation = 'operation', result?: T) {
    return (error: any): Observable<T> => {

      // TODO: send the error to remote logging infrastructure
      console.error(error); // log to console instead

      // Let the app keep running by returning an empty result.
      return of(result as T);
    };
  }
}