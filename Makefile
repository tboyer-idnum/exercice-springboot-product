#!make
.DEFAULT_GOAL := help
.PHONY: help
SHELL := /bin/bash

.PHONY: help
help:
	@echo "\033[33mUsage:\033[0m\n  make [target] [arg=\"val\"...]\n\n\033[33mTargets:\033[0m"
	@grep -E '^[a-zA-Z0-9_-]+:.*?## .*$$' Makefile| sort | awk 'BEGIN {FS = ":.*?## "}; {printf "  \033[32m%-15s\033[0m %s\n", $$1, $$2}'
.PHONY: up
up: ## Start app and verify dependencies & db
	docker compose up --build --detach --remove-orphans
	docker compose logs -f app | ccze -m ansi

.PHONY: test
test: ## Start app and verify dependencies & db
	docker compose exec app mvn test