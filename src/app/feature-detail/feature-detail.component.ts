import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { EventBusService, APP_TITLE_CHANGE } from '../event-bus.service';
import { Feature } from '../models/feature';
import { FeatureService } from '../feature.service';

@Component({
  selector: 'app-feature-detail',
  templateUrl: './feature-detail.component.html',
  styleUrls: ['./feature-detail.component.css']
})
export class FeatureDetailComponent implements OnInit {

  name: string;
  heading: string;
  feature: Feature;

  constructor(
    private route: ActivatedRoute,
    private eventBusService: EventBusService,
    private featureService: FeatureService
  ) { }

  ngOnInit() {
    this.name = this.route.snapshot.params['name'];
    this.heading = `Feature details for ${this.name}`;
    this.eventBusService.emit(APP_TITLE_CHANGE, this.heading);
    this.feature = this.featureService.getFeature(this.name);
  }

}
