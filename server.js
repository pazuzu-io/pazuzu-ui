// get dependencies
const express = require('express');
const path = require('path');
const http = require('http');
const bodyParser = require('body-parser');
const proxy = require('http-proxy-middleware');
const passport = require('passport');
const OAuth2Strategy = require('passport-oauth2');

// get client credentials the ugly way
const client = require('./client.json');

// configure passport
passport.use(

  // The strategy requires a verify callback, which receives an access token and profile,
  // and calls "cb" providing a user.

  new OAuth2Strategy({
    authorizationURL: 'https://auth.zalando.com/z/oauth2/authorize?realm=employees',
    tokenURL: 'https://auth.zalando.com/z/oauth2/access_token?realm=employees',
    clientID: client.client_id || '',
    clientSecret: client.client_secret || '',
    callbackURL: 'https://pazuzu-ui.mentoring.zalan.do'
  }, function(accessToken, refreshToken, profile, cb) {
    console.log(`Access Token ${accessToken}, Refresh Token ${refreshToken}`);
    window.localStorage.setItem('access_token', accessToken);
    window.localStorage.setItem('refresh_token', refreshToken);
  })

);

// create server
const app = express();

// parsers for POST data
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: false }));

// point static path to dist
app.use(express.static(path.join(__dirname, 'dist')));

// set our api routes
app.use('/api', proxy({
  target: 'https://pazuzu-registry.mentoring.zalan.do',
  changeOrigin: true,
  logLevel: 'debug'
}));

// define routes (deep links) to protect
// TODO: add canActivate guards inside of angular app as well
app.get('/features/create', passport.authenticate('oauth2'));

// catch all other routes and return the index file
app.get('*', (req, res) => {
  res.sendFile(path.join(__dirname, 'dist/index.html'));
});

// get port from environment and store in Express.
const port = process.env.PORT || '3000';
app.set('port', port);

// create HTTP server.
const server = http.createServer(app);

// Listen on provided port, on all network interfaces.
server.listen(port, () => console.log(`Server running on http://localhost:${port}`));
