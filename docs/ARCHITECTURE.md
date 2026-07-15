# Architecture Overview

The framework is organized into independent modules so that each responsibility remains isolated and easy to extend.

## Core Modules

- app: application entry point and sample UI
- core: initialization, config loading, logging, version management
- api: stable interface for modules
- common: shared models and utilities
- hooks: generic hook engine and callback abstractions
- manager: module discovery, enable/disable, compatibility checks
- service: privileged service bridge and process tracking
- bridge: inter-process communication abstraction
- daemon: long-running background service abstraction
- example-module: demonstration module

## User lifecycle focus

The initial implementation models the research workflow for Android 10 user lifecycle analysis:

1. Capture invocation metadata for user start and stop requests.
2. Store caller UID, PID, package, process, stack trace, binder caller, and reason.
3. Emit logs to /data/local/tmp/user_stop.log.
4. Support future interception policies such as observe, allow, block, delay, and modify.

## Future expansion

The architecture leaves room for:

- Android 11+ compatibility layers
- native hooks
- Zygisk integration
- advanced monitoring pipelines
