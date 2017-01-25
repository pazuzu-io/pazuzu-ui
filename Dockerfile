FROM registry.opensource.zalan.do/stups/node:7.4-cd34

# create app directory
RUN mkdir -p /usr/src/app
WORKDIR /usr/src/app

# install app dependencies
RUN npm install express body-parser http-proxy-middleware passport passport-oauth2

# bundle app source
COPY dist /usr/src/app/dist
COPY server.js /usr/src/app/server.js
COPY client.json /usr/src/app/client.json

# copy scm-source.json
COPY scm-source.json /

# expose port
EXPOSE 3000

# run command
CMD [ "node", "server" ]
