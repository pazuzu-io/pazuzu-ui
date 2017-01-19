import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpModule } from '@angular/http';
import { RouterModule } from '@angular/router';

import 'hammerjs';

import 'rxjs/add/operator/filter';
import 'rxjs/add/operator/map';

import { APP_PROVIDERS } from './app.providers';
import { APP_ROUTES } from './app.routes';

import { AppComponent } from './app.component';
import { FeatureListComponent } from './feature-list/feature-list.component';
import { FeatureDetailComponent } from './feature-detail/feature-detail.component';

import { AceEditorComponent } from 'ng2-ace-editor';
import { AceEditorDirective } from 'ng2-ace-editor';

@NgModule({
  declarations: [
    AppComponent,
    FeatureListComponent,
    FeatureDetailComponent,
    AceEditorComponent,
    AceEditorDirective
  ],
  imports: [
    BrowserModule,
    RouterModule.forRoot(APP_ROUTES),
    FormsModule,
    HttpModule
  ],
  providers: APP_PROVIDERS,
  bootstrap: [AppComponent]
})
export class PazuzuUIAppModule { }
