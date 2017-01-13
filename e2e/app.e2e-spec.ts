import { PazuzuUiPage } from './app.po';

describe('Pazuzu UI App', function() {
  let page: PazuzuUiPage;

  beforeEach(() => {
    page = new PazuzuUiPage();
  });

  it('should display message saying app works', () => {
    page.navigateTo();
    expect(page.getParagraphText()).toEqual('app works!');
  });
});
