CONTAINER=pazuzu-ui-nginx

IMAGE=pazuzu-ui-nginx

# be aware that when changing that to also change the URL in yourturn
HOSTNAME?=localhost

BACKEND_IP?=$(shell ip addr show en0 | grep "inet "  | sed -E "s/.*inet ([[:digit:]\.]+).*/\1/")

all: build run

.PHONY: build
build:
	cd build && docker build -t ${IMAGE} .
	docker images ${IMAGE}

.PHONY: remove
remove:
	docker stop ${CONTAINER} && docker rm -fv ${CONTAINER} || true

.PHONY: run
run: remove
	docker run -dit --name=${CONTAINER} \
		-p 8080:8080 \
		-e HOSTNAME=${HOSTNAME} \
		-e BACKEND_IP=${BACKEND_IP} \
		${IMAGE}

.PHONY: logs
logs:
	docker logs -f ${CONTAINER}

.PHONY: shell
shell:
	docker exec -it ${CONTAINER} /bin/bash
