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

Registry
--------

> The following describes API version 0.0.4! More up-to-date examples will follow.

### Persistance

It’s using Spring Boot and JPA as ORM API.

### API (Outdated)

> Please have a look at the Swagger file in project `pazuzu-registry`.

#### Features

##### Create

```bash
curl -s -X POST --header 'Content-Type: application/json' --header 'Accept: application/json' -d '{ "meta": { "name": "java", "description": "openjdk", "author": "mlehmann1" }, "snippet": "FROM openjdk:7" }' 'http://localhost:8080/api/features'
curl -s -X POST --header 'Content-Type: application/json' --header 'Accept: application/json' -d '{ "meta": { "name": "scala", "description": "scala", "author": "mlehmann1", "dependencies": ["java"] }, "snippet": "FROM hseeberger/scala-sbt" }' 'http://localhost:8080/api/features'
```

##### Read

```bash
curl -s -X GET --header 'Content-Type: application/json' --header 'Accept: application/json' 'http://localhost:8080/api/features'
curl -s -X GET --header 'Content-Type: application/json' --header 'Accept: application/json' 'http://localhost:8080/api/resolved-features?names=java'
curl -s -X GET --header 'Content-Type: application/json' --header 'Accept: application/json' 'http://localhost:8080/api/resolved-features?names=java,scala'
curl -s -X GET --header 'Content-Type: application/json' --header 'Accept: application/json' 'http://localhost:8080/api/resolved-features?names=java&names=scala'
```

With pagination returning an additional header X-TOTAL-COUNT:

```bash
curl -s -i -X GET --header 'Content-Type: application/json' --header 'Accept: application/json' 'http://localhost:8080/api/features?offset=0&limit=0'
```

##### Update

```bash
curl -s -X PUT --header 'Content-Type: application/json' --header 'Accept: application/json' -d '{ "snippet": "FROM openjdk:8" }' 'http://localhost:8080/api/features/java'
```

##### Delete

```bash
curl -s -X DELETE --header 'Content-Type: application/json' --header 'Accept: application/json' 'http://localhost:8080/api/features/scala'
```

#### Feature Metas

##### Read

```bash
curl -s -X GET --header 'Content-Type: application/json' --header 'Accept: application/json' 'http://localhost:8080/api/feature-metas'
curl -s -X GET --header 'Content-Type: application/json' --header 'Accept: application/json' 'http://localhost:8080/api/feature-metas/java'
```

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
