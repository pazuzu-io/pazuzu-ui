import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { EventBusService, APP_TITLE_CHANGE } from '../event-bus.service';

@Component({
  selector: 'app-feature-detail',
  templateUrl: './feature-detail.component.html',
  styleUrls: ['./feature-detail.component.css']
})
export class FeatureDetailComponent implements OnInit {

  id: string;

  constructor(
    private eventBusService: EventBusService,
    private route: ActivatedRoute
  ) { }

  ngOnInit() {
    this.eventBusService.emit(APP_TITLE_CHANGE, 'Feature Details');
    this.id = this.route.snapshot.params['id'];
  }

}
