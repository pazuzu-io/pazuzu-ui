import { Component, OnInit, OnDestroy } from '@angular/core';
import { Router, ActivatedRoute, NavigationEnd } from '@angular/router';
import { Location } from '@angular/common';

import { Subscription } from 'rxjs';

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
  feature: Feature;

  update() {
    this.name = this.route.snapshot.params['name'];
    this.feature = this.featureService.getFeature(this.name);
    this.heading = `Feature details for ${this.name}`;
    this.eventBusService.emit(APP_TITLE_CHANGE, this.heading);
  }

  goBack() {
    this.location.back();
  }

  constructor(
    private router: Router,
    private route: ActivatedRoute,
    private location: Location,
    private eventBusService: EventBusService,
    private featureService: FeatureService
  ) {

    this.sub = router.events.subscribe((val) => {
      if (val instanceof NavigationEnd) {
        this.update();
      }
    });

  }

  ngOnInit() {
    this.update();
  }

  ngOnDestroy() {
    this.sub.unsubscribe();
  }

}
