import { Injectable } from '@angular/core';
import { Subject } from 'rxjs/Subject';

export const APP_TITLE_CHANGE: string = 'APP_TITLE_CHANGE';

export interface EventBusArgs {
  type: string,
  data: any
}

@Injectable()
export class EventBusService {

  private messages$ = new Subject<EventBusArgs>();

  constructor() { }

  emit(eventType: string, data: any) {
    this.messages$.next({type: eventType, data: data});
  }

  observe(eventType: string) {
    return this.messages$
      .filter(args => args.type === eventType)
      .map(args => args.data);
  }

}
