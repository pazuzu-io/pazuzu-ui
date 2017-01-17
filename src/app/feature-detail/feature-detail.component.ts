import { Component, OnInit, OnDestroy } from '@angular/core';
import { Router, ActivatedRoute, NavigationEnd } from '@angular/router';
import { Location } from '@angular/common';

import { Subscription, Observable } from 'rxjs';

import { EventBusService, APP_TITLE_CHANGE } from '../event-bus.service';
import { Feature } from '../models/feature';
import { FeatureService } from '../feature.service';

@Component({
  selector: 'app-feature-detail',
  templateUrl: './feature-detail.component.html',
  styleUrls: ['./feature-detail.component.css']
})
export class FeatureDetailComponent implements OnInit, OnDestroy {

  sub: Subscription;
  name: string;
  heading: string;
  feature: Observable<Feature>;

  /**
   * go back using Location API
   * @returns {void} nothing
   */
  goBack() {

    // go back
    this.location.back();

  }

  /**
   * constructor
   * @param {Router} router
   * @param {ActivatedRoute} route
   * @param {Location} location
   * @param {EventBusService} eventBusService
   * @param {FeatureService} featureService
   * @returns {void} nothing
   */
  constructor(
    private router: Router,
    private route: ActivatedRoute,
    private location: Location,
    private eventBusService: EventBusService,
    private featureService: FeatureService
  ) { }

  /**
   * on init handler
   * load feature
   * @returns {void} nothing
   */
  ngOnInit() {

    // subscribe to param changes if somebody manually changes them
    this.sub = this.router.events.subscribe((val) => {

      // if event is of type NavigationEnd update internal state
      if (val instanceof NavigationEnd) {

        // get name parameter
        this.name = this.route.snapshot.params['name'];

        // update heading and title
        this.heading = `Feature details for ${this.name}`;
        this.eventBusService.emit(APP_TITLE_CHANGE, this.heading);

        // get feature
        this.featureService.get(this.name)
          .subscribe(
            feature => this.feature = feature,
            () => this.router.navigate(['/features/list'])
          );

      }

    });

  }

  /**
   * on destroy handler
   * unsubscribe from parameter changes
   * @returns {void} nothing
   */
  ngOnDestroy() {
    this.sub.unsubscribe();
  }

}
