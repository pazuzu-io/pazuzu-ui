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
   * get all features for given page and limit
   * @param {number} page
   * @param {number} limit
   * @returns {Observable<Array<Feature>>}
   */
  /*
  getAll(page = 1, limit = 10) {

    // set limit
    this.limit = limit;

    // set offset
    let offset = (page - 1) * limit;

    // set search params
    let params: URLSearchParams = new URLSearchParams();
    params.set('limit', limit.toString());
    params.set('offset', offset.toString());

    // trigger request
    return this.http.get(`/api/features`, {search: params})
      .map(res => {

        // update count and pages
        this.count = +res.headers.get('x-total-count') || 0;
        this.pages = Math.max(Math.ceil(this.count / this.limit), 1);

        // return features as json
        return res.json();

      });

  }
  */

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

}
