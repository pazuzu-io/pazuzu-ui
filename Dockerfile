FROM registry.opensource.zalan.do/stups/node:7.4-cd34

# create app directory
RUN mkdir -p /usr/src/app
WORKDIR /usr/src/app

# install app dependencies
RUN npm install express body-parser http-proxy-middleware

# bundle app source
COPY dist /usr/src/app/dist

# copy meta
COPY scm-source.json /

# copy server source
# ...

# expose port
EXPOSE 3000

# run command
CMD [ "node", "server" ]
