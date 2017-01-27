const express = require('express');
const path = require('path');
const http = require('http');
const oauthLib = require('oauth-lib/server');
const proxy = require('http-proxy-middleware');

// create server
const app = express();

// configure services middleware
const servicesMiddleware = oauthLib.servicesMiddleware({
  appId: 'pazuzu-ui',
  bucketName: 'zalando-stups-mint-187355903146-eu-central-1',
  devMode: process.env.NODE_ENV === 'development'
});

// configure protector middleware
const protector = oauthLib.protector({
  appId: 'pazuzu-ui',
  bucketName: 'zalando-stups-mint-187355903146-eu-central-1',
  credentialsDir: './meta/credentials',
  redirectUri: 'https://pazuzu-ui.mentoring.zalan.do',
  employeesTokeninfo: 'https://auth.zalando.com/oauth2/tokeninfo',
  locations: [oauthLib.BEARER_TOKEN],
  devMode: process.env.NODE_ENV === 'development'
});


// CONFIG ---

// point static path to dist
app.use(express.static(path.join(__dirname, 'dist')));

// use services middleware for all api calls
app.use('/api/\*', servicesMiddleware({
  scope: ['uid', 'cn']
}));

/*
// proxy to backend api
app.use('/api', proxy({
  target: 'https://pazuzu-registry.mentoring.zalan.do',
  changeOrigin: true,
  logLevel: 'debug'
}));
*/


// REQUESTS ---

// protected api method and route
/*
app.post('/api/features', protector.middleware({
  handleErrors: true
}), (req, res, next) => {
  console.log(req.headers);
  console.log(req.user);
  // return next();
});
*/
app.post(
  '/api/features',
  protector.middleware({
    handleErrors: true
  }),
  (req, res, next) => {
    console.log(req.headers);
    return next();
  },
  proxy({
    target: 'https://pazuzu-registry.mentoring.zalan.do',
    changeOrigin: true,
    logLevel: 'debug'
  })
);

// delete token
app.get('/logout', protector.logoutHandler);

// catch all other routes and return the index file
app.get('*', (req, res) => {
  res.sendFile(path.join(__dirname, 'dist/index.html'));
});


// START ---

// get port from environment and store in Express.
const port = process.env.PORT || '3000';
app.set('port', port);

// create HTTP server.
const server = http.createServer(app);

// Listen on provided port, on all network interfaces.
server.listen(port, () => console.log(`Server running on http://localhost:${port}`));
