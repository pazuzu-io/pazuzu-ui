import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { Subscription, Subject, Observable } from 'rxjs';

import { EventBusService, APP_TITLE_CHANGE } from '../event-bus.service';
import { Feature } from '../models/feature';
import { FeatureService } from '../feature.service';

@Component({
  selector: 'app-feature-list',
  templateUrl: './feature-list.component.html',
  styleUrls: ['./feature-list.component.css']
})
export class FeatureListComponent implements OnInit, OnDestroy {

  sub: Subscription;

  heading: string;
  featureHeadlineMapping: any = {
    '=0': 'No feature',
    '=1': 'One feature',
    'other': '# features'
  };

  page: number = 1;
  pages: number[];

  limit: number = 10;

  features$: Observable<Array<Feature>>;

  featureCount: number;
  pageCount: number;

  /**
   * retrieve features for one page (value of `this.page`)
   * @returns {void} nothing
   */
  getData() {

    this.features$ =
      this.featureService.getAll(this.page, this.limit)
        .map((features) => {

          // update feature and page count
          this.featureCount = this.featureService.getFeatureCount();
          this.pageCount = this.featureService.getPageCount();

          // limit page to valid values
          if (this.page < 1 || this.page > this.pageCount) {
            this.page = Math.max(this.page, 1);
            this.page = Math.min(this.page, this.pageCount);
            this.goTo(this.page);
          }

          // TODO: cut out pages in between first, current page and last page if there are more than 5
          this.pages =
            Array.apply(null, new Array(this.pageCount)).map((_, i) => i + 1);

          return features;
        });

  }

  /**
   * update query parameter to given value and reload data
   * @param {number} page
   * @returns {void} nothing
   */
  goTo(page: number) {

    // update query parameter
    this.router.navigate(['/features/list'], {queryParams: {page: page, limit: this.limit}}).then(() => {

      // get features and pagination info
      this.getData();

    });

  }

  /**
   * set limit to given value
   * @param {number} limit
   * @returns {void} nothing
   */
  limitTo(limit: number) {

    // set new limit
    this.limit = limit;

    // update data
    this.goTo(this.page);

  }

  /**
   * constructor
   * @param {Router} router
   * @param {ActivatedRoute} route
   * @param {EventBusService} eventBusService
   * @param {FeatureService} featureService
   * @returns {void} nothing
   */
  constructor(
    private router: Router,
    private route: ActivatedRoute,
    private eventBusService: EventBusService,
    private featureService: FeatureService
  ) {

    // update query parameters if they already exist
    if (typeof this.route.snapshot.queryParams['page'] !== 'undefined') {
      this.page = +this.route.snapshot.queryParams['page'];
    }

    if (typeof this.route.snapshot.queryParams['limit'] !== 'undefined') {
      this.limit = +this.route.snapshot.queryParams['limit'];
    }

    // get features and pagination info
    this.getData();

    // update heading and title
    this.heading = 'Feature list';
    this.eventBusService.emit(APP_TITLE_CHANGE, this.heading);

  }

  /**
   * on init handler
   * subscribe to query parameter changes to sanitize it and occasionally call 'goTo()`
   * @returns {void} nothing
   */
  ngOnInit() {

    // subscribe to query param changes
    this.sub = this.route.queryParams
      .subscribe(params => {

        // convert page param to number
        this.page = +params['page'] || 1;

        // in case we sanitized page value update query parameter
        if (this.page !== +params['page'] && typeof params['page'] !== 'undefined') {
          this.goTo(this.page);
        }

      });

  }

  /**
   * on destroy handler
   * unsubscribe from query parameter changes
   * @returns {void} nothing
   */
  ngOnDestroy() {

    // unsubscribe from query param changes
    this.sub.unsubscribe();

  }

}
