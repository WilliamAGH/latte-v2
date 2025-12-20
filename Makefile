SHELL := /bin/sh

.PHONY: build
build:
	./gradlew build --rerun-tasks --no-configuration-cache

.PHONY: test
test:
	./gradlew test --rerun-tasks --no-configuration-cache

.PHONY: clean
clean:
	./gradlew clean --no-configuration-cache
