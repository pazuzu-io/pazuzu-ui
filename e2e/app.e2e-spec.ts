import { PazuzuUiPage } from './app.po';

describe('pazuzu-ui App', function() {
  let page: PazuzuUiPage;

  beforeEach(() => {
    page = new PazuzuUiPage();
  });

  it('should display message saying app works', () => {
    page.navigateTo();
    expect(page.getParagraphText()).toEqual('app works!');
  });
});
