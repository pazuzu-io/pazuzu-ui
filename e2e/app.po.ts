import { browser, element, by } from 'protractor';

export class PazuzuUiPage {
  navigateTo() {
    return browser.get('/');
  }

  getParagraphText() {
    return element(by.css('app-pazuzu h1')).getText();
  }
}
