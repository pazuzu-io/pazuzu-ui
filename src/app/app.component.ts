import { Component, OnInit } from '@angular/core';
import { Title } from '@angular/platform-browser';

import { EventBusService, APP_TITLE_CHANGE } from './event-bus.service';

@Component({
  selector: 'pazuzu-ui-app',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {

  title = 'Pazuzu UI';

  constructor(
    private eventBusService: EventBusService,
    private titleService: Title
  ) {

  }

  ngOnInit() {
    this.eventBusService
      .observe(APP_TITLE_CHANGE)
      .subscribe(title => {
        this.titleService.setTitle(title + ' | ' + this.title);
      });
  }

}
