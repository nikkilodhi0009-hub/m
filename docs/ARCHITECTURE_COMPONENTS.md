# Component architecture guide

## HookEngine
Purpose:
- Coordinate hook registration, lifecycle state, diagnostics, and event dispatch.

Fit in architecture:
- Top-level orchestrator.
- Should delegate backend-specific execution to RuntimeHookBackend.

Design improvements:
- Keep it thin and orchestration-focused.
- Avoid mixing backend logic and diagnostics with core scheduling.

## RuntimeHookBackend
Purpose:
- Provide a concrete backend adapter for runtime operations.

Fit in architecture:
- Backend implementation behind HookBackend.
- Should not own logging or configuration.

Design improvements:
- Define explicit capabilities and lifecycle semantics.
- Keep install/verify/remove/health/rollback behavior consistent.

## HookLifecycleManager
Purpose:
- Manage hook lifecycle state transitions.

Fit in architecture:
- Central state machine for hook lifecycle.

Design improvements:
- Keep state transitions explicit and testable.
- Make snapshots deterministic and safe for concurrent access.

## HookRegistry
Purpose:
- Store active registrations.

Fit in architecture:
- Shared registry for lookup and removal.

Design improvements:
- Keep it thread-safe and minimal.
- Avoid exposing mutable internals directly.

## HookDiagnostics
Purpose:
- Record structured diagnostics and health information.

Fit in architecture:
- Observation layer for the hook runtime.

Design improvements:
- Make records structured and queryable.
- Keep the observer interface independent from logging output.

## ReflectionHelper
Purpose:
- Resolve classes, methods, and constructors.

Fit in architecture:
- Support utility for the backend and engine.

Design improvements:
- Keep resolution logic deterministic and signature-aware.
- Avoid embedding policy decisions inside it.

## AndroidVersionCompat
Purpose:
- Isolate Android API-version-specific behavior.

Fit in architecture:
- Compatibility boundary between runtime logic and Android version differences.

Design improvements:
- Keep it narrow and explicit.
- Avoid hard-coded behavior scattered across classes.

## ConfigLoader
Purpose:
- Create and manage framework configuration files.

Fit in architecture:
- Shared configuration layer.

Design improvements:
- Add validation and versioning.
- Support reloads without forcing a full restart.

## ModuleManager
Purpose:
- Discover and manage optional modules.

Fit in architecture:
- Extension boundary for the framework.

Design improvements:
- Keep module loading pluggable and testable.
- Avoid coupling it to hook runtime behavior.

## UserLifecycleMonitor
Purpose:
- Capture lifecycle events and timeline state.

Fit in architecture:
- Observation boundary for lifecycle monitoring.

Design improvements:
- Keep event storage efficient and bounded.
- Expose snapshots for reporting and diagnostics.
