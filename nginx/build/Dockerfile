FROM nginx:1.9.2

RUN apt-get update && \
    apt-get install -y gettext && \
    apt-get clean

ADD nginx.conf.tmpl /tmp/nginx.conf.tmpl

ADD ca.crt /ssl/ca.crt
ADD ca.key /ssl/ca.key
ADD cert.crt /ssl/cert.crt
ADD cert.key /ssl/cert.key

ADD start /start

CMD ["/start"]
