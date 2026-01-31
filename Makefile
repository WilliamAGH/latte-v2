SHELL := /bin/sh

.PHONY: build
build:
	./gradlew build -x test --rerun-tasks --no-configuration-cache

.PHONY: test
test:
	./gradlew test -q --rerun-tasks --no-configuration-cache

.PHONY: clean
clean:
	./gradlew clean --no-configuration-cache
