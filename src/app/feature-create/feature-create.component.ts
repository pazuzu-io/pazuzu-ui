import { Component, OnInit, OnDestroy } from '@angular/core';
import { Router, ActivatedRoute, NavigationEnd } from '@angular/router';
import { Location } from '@angular/common';

import { Subscription, Observable } from 'rxjs';

import { EventBusService, APP_TITLE_CHANGE } from '../event-bus.service';
import { Feature } from '../models/feature';
import { FeatureService } from '../feature.service';

@Component({
  selector: 'app-feature-create',
  templateUrl: './feature-create.component.html',
  styleUrls: ['./feature-create.component.css']
})
export class FeatureCreateComponent implements OnInit, OnDestroy {

  private _routerEventsSub: Subscription;

  heading: string;

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
    this._routerEventsSub = this.router.events
      .subscribe(event => {

        // if event is of type NavigationEnd update internal state
        if (event instanceof NavigationEnd) {

          // update heading and title
          this.heading = `Create feature`;
          this.eventBusService.emit(APP_TITLE_CHANGE, this.heading);

        }

      });

  }

  /**
   * on destroy handler
   * @returns {void} nothing
   */
  ngOnDestroy() {

    // unsubscribe
    this._routerEventsSub.unsubscribe();

  }

}
