// get dependencies
const express = require('express');
const path = require('path');
const http = require('http');
const bodyParser = require('body-parser');
const proxy = require('http-proxy-middleware');

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
