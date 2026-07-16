# Production readiness plan

## Priority 1: runtime backend architecture
- Keep the runtime backend behind a stable abstraction.
- Make install/verify/remove/health/rollback semantics explicit.
- Preserve observability without coupling runtime logic to logging.

## Priority 2: lifecycle and registry
- Keep hook lifecycle transitions explicit: pending, installed, enabled, disabled, failed, rolled back.
- Ensure registry operations remain thread-safe and observable.

## Priority 3: diagnostics and observability
- Record structured diagnostics for registration, verification, rollback, and health checks.
- Keep logs and diagnostics decoupled from core state transitions.

## Priority 4: compatibility and configuration
- Isolate Android-version-specific behavior in AndroidVersionCompat.
- Support configuration hot-reload with validation and version awareness.

## Priority 5: module loading and testing
- Keep module discovery composable and testable.
- Continue adding unit and integration tests as the framework evolves.
