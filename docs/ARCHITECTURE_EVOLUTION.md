# Architecture evolution plan

## Goals

The framework should remain an observability and lifecycle monitoring platform for Android, with a modular architecture that is easy to extend and test.

## Architectural layers

### 1. Core orchestration layer
- HookEngine remains the top-level orchestrator.
- Its responsibility is to coordinate registration, lifecycle state, diagnostics, and dispatching.
- It should not own backend-specific logic or policy evaluation directly.

### 2. Runtime backend layer
- RuntimeHookBackend should remain the concrete adapter for runtime integration.
- It should expose stable install/verify/remove/health semantics.
- It should not be responsible for logging or module discovery.

### 3. Lifecycle and registry layer
- HookLifecycleManager manages hook state transitions.
- HookRegistry stores active registrations in a thread-safe structure.
- Both should remain independent of logging and policy concerns.

### 4. Diagnostics and observability layer
- HookDiagnostics records structured events and health state.
- Logger and LifecycleLogWriter remain the output sinks.
- Diagnostics should remain optional and low-overhead.

### 5. Compatibility layer
- AndroidVersionCompat should encapsulate Android-version-aware behavior.
- The layer should be the only place where API-specific differences are handled.

### 6. Configuration and module layer
- ConfigLoader should own config file creation and validation.
- ModuleManager should own discovery and lifecycle of optional modules.
- Neither should embed hook runtime behavior.

## Recommended improvements

### Modularity
- Split HookEngine into smaller collaborators over time.
- Keep one responsibility per component.
- Use interfaces for backend selection and diagnostics sinks.

### Thread safety
- Keep mutable state behind synchronized or concurrent primitives.
- Prefer immutable snapshots for diagnostics and reporting.
- Avoid shared mutable lists in the engine path.

### Diagnostics
- Record structured events with status, timestamp, target, backend, and reason.
- Keep health-check outputs deterministic and easy to inspect.

### Configuration
- Support hot reload with validation and version awareness.
- Keep defaults explicit and documented.

### Compatibility
- Introduce version-aware helpers rather than hard-coded assumptions.
- Make Android-specific behavior isolated and testable.

### Testing strategy
- Unit tests for lifecycle transitions and config loading.
- Integration tests for registry and diagnostics.
- Future instrumentation tests should run only on Android device/emulator environments.
