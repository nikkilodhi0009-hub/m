# Architecture Overview

The framework is organized into independent modules so that each responsibility remains isolated and easy to extend.

## Core Modules

- app: application entry point and sample UI
- core: initialization, config loading, logging, version management
- api: stable interface for modules
- common: shared models and utilities
- hooks: generic hook engine, lifecycle state, diagnostics, and callback abstractions
- manager: module discovery, enable/disable, and compatibility checks
- service: privileged service bridge and process tracking
- bridge: inter-process communication abstraction
- daemon: long-running background service abstraction
- example-module: demonstration module

## Architectural layers

The current framework is structured around the following layers:

1. Orchestration layer
   - HookEngine coordinates registration, lifecycle state, diagnostics, and event dispatch.
2. Runtime backend layer
   - RuntimeHookBackend remains the concrete runtime adapter, even if it is only a placeholder today.
3. Lifecycle and registry layer
   - HookLifecycleManager and HookRegistry manage state and registration visibility.
4. Diagnostics and observability layer
   - Logger, LifecycleLogWriter, HookDiagnostics, and UserLifecycleMonitor capture runtime information.
5. Compatibility layer
   - AndroidVersionCompat centralizes Android-version-specific behavior.
6. Configuration and module layer
   - ConfigLoader and ModuleManager manage configuration and optional module discovery.

## User lifecycle focus

The framework models the research workflow for Android 10 user lifecycle analysis:

1. Capture invocation metadata for user start and stop requests.
2. Store caller UID, PID, package, process, stack trace, binder caller, and reason.
3. Emit logs to /data/local/tmp/framework.log.
4. Support future policies such as observe, allow, block, delay, and modify.

## Design principles

- Keep backend-specific implementation behind stable abstractions.
- Keep state transitions explicit and observable.
- Keep thread-safety and diagnostics first-class concerns.
- Preserve modularity so that runtime backends and module loaders can evolve independently.

## Future expansion

The architecture remains suitable for:

- Android 11+ compatibility layers
- additional runtime backends
- richer monitoring pipelines
- safer module loading and configuration hot-reload
