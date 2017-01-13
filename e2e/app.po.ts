import { browser, element, by } from 'protractor';

export class PazuzuUiPage {
  navigateTo() {
    return browser.get('/');
  }

  getToolbarTitle() {
    return element(by.css('app-pazuzu md-toolbar md-toolbar-row span:first-child')).getText();
  }
}
