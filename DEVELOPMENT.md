# pazuzu-ui

<img align="right" height="300" src="/pazuzu-logo.png">

What is Pazuzu?
---------------

Pazuzu is a tool that builds Docker images from feature snippets, while
resolving all dependencies between them. One of the common use cases is
Continuous Integration environment, where jobs require specific tooling present
for building and testing. Pazuzu can significantly ease that process, by letting user
choose from a wide selection of predefined Dockerfile snippets that represent
those dependencies (e.g. Golang, Python, Android SDK, customized NPM installs).

What is Pazuzu UI?
------------------

UI for Pazuzu Registry where you will be able to create new features as well as
approve them.

This is a single-page application implemented in TypeScript using
[`Angular 2`](https://angular.io).

Development
-----------

## Prerequisites

Install [`Angular CLI`](https://cli.angular.io):

```bash
$ npm install -g angular-cli
```

## Create application

Use `ng` command to create application in new folder with `new` or in existing folder with `init`:

```bash
$ ng init
```

Be aware to not overwrite `README.md`.

## Run application in dev mode

Open application with live reload capabilities:

```bash
$ ng serve
```

## Test application

Continuously run unit tests:

```bash
$ ng test
```

Continuously run end-to-end tests (start application using `ng serve` before):

```bash
$ ng e2e
```

## Build application

Create distribution for development or production environment:

```bash
$ ng build [--prod]
```

Developing locally
------------------

This is a small guide that hopefully demonstrates how to get the
[pazuzu-registry](https://github.com/zalando/pazuzu-registry) and the
[pazuzu-ui](https://github.com/zalando/pazuzu-ui) running locally with OAuth
enabled.

1. Clone both repositories

In one base directory run:

    git clone git@github.com:zalando/pazuzu-ui.git
    git clone git@github.com:zalando/pazuzu-registry.git

2. Create NGINX docker container

In order to get the SSL working we need nginx to terminate the ssl requests and
forward them to our running UI and registry processes.

    cd pazuzu-ui/nginx
    sudo make build
    sudo BACKEND_IP=$YOUR_EXTERNAL_IP make run

This will create an nginx proxy that forwards traffic from
`https://localhost:8080` to `http://$YOUR_EXTERNAL_IP:3449`, i.e. the running
figwheel repl. It will also forward traffic from `https://localhost:8081` to
`http://$YOUR_EXTERNAL_IP:8082`, i.e. the running registry.

3. Request client tokens from mint

In order to get both running we need client ids from mint:

    berry -a pazuzu-registry -m $MINT_BUCKET --once -f /dev/null ~/.berry/pazuzu-registry
    cd pazuzu-ui
    berry -a pazuzu-ui-dev -m $MINT_BUCKET --once -f /dev/null credentials

4. Start the registry

First, make sure your LDAP uid is in
`pazuzu-registry/src/main/resources/config/application-dev.yml`:

    pazuzu:
        registry:
            admins:
                dtruemper

Now build and start the registry:

    mvn package
    java -Dserver.port=8082 -Dspring.profiles.active=dev,oauth

You can also run the registry inside your IDE with the equivalent command line
args.

5. Start the UI

Inside EMACS simply call `cider-jack-in-clojurescript`. In the command line
this will result in similar experience:

    lein figwheel dev

Happy hacking!

Packaging
---------

To build a standalone runnable jar

    export BACKEND_ENPOINT=https://backend-registry-endpoint.ok
    lein uberjar
    java -jar ./target/pazuzu-ui.jar

if no BACKEND_ENDPOINT environment variable is specified,
the default backend endpoint will be http://localhost:8080

License
-------

The MIT License (MIT)
Copyright © 2017 Zalando SE, https://tech.zalando.com

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the “Software”), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.