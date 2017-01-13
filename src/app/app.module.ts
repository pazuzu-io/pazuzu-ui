import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpModule } from '@angular/http';
import { RouterModule } from '@angular/router';
import { MaterialModule } from '@angular/material';

import 'hammerjs';

import { APP_PROVIDERS } from './app.providers';
import { APP_ROUTES } from './app.routes';

import { AppComponent } from './app.component';
import { FeatureListComponent } from './feature-list/feature-list.component';

@NgModule({
  declarations: [
    AppComponent,
    FeatureListComponent
  ],
  imports: [
    BrowserModule,
    RouterModule.forRoot(APP_ROUTES),
    FormsModule,
    HttpModule,
    MaterialModule.forRoot()
  ],
  providers: APP_PROVIDERS,
  bootstrap: [AppComponent]
})
export class AppModule { }
