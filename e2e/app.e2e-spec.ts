import { PazuzuUiPage } from './app.po';

describe('Pazuzu UI App', function() {
  let page: PazuzuUiPage;

  beforeEach(() => {
    page = new PazuzuUiPage();
  });

  it(`should display app title 'Pazuzu UI'`, () => {
    page.navigateTo();
    expect(page.getToolbarTitle()).toEqual('Pazuzu UI'.toUpperCase());
  });
});
