import { Injectable } from '@angular/core';
import { Http, URLSearchParams } from '@angular/http';

import { Feature } from './models/feature';

import { Observable, Subject } from 'rxjs';

@Injectable()
export class FeatureService {

  // total number of features
  private _total = new Subject<number>();

  /**
   * get total number of features
   * @returns {number}
   */
  total() {
    return this._total;
  }

  /**
   * constructor
   * @param {Http} http
   * @returns {void} nothing
   */
  constructor(
    private http: Http
  ) { }

  /**
   * get features by parameters
   * @param {URLSearchParams} params
   * @returns {Observable<Array<Feature>>}
   */
  list(params: URLSearchParams) {

    // trigger request
    return this.http.get(`/api/features`, {search: params})
      .map(res => {
        // TODO: rework this as soon as API is changed
        this._total.next(+res.headers.get('x-total-count') || 0);
        return res.json();
      });

  }

  /**
   * get feature by name
   * @param {string} name
   * @returns {Feature}
   */
  get(name: string) {

    // trigger request
    return this.http.get(`/api/features/${name}`)
      .map(res => res.json());

  }

  /**
   * create new feature
   * @param {Feature} feature
   * @return {Observable<R>}
   */
  create(feature: Feature) {

    // trigger request
    return this.http.post(`/api/features`, feature)
      .map(res => res.json());

  }

  /**
   * delete feature by name
   * @param {string} name
   * @return {Observable<R>}
   */
  delete(name: string) {

    // trigger request
    return this.http.delete(`/api/features/${name}`)
      .map(res => res.json());

  }

}
