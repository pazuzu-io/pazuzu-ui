import { browser, element, by } from 'protractor';

export class PazuzuUiPage {
  navigateTo() {
    return browser.get('/');
  }

  getToolbarTitle() {
    return element(by.css('pazuzu-ui-app a.brand-logo')).getText();
  }
}
