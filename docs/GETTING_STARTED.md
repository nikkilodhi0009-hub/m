# Getting Started

## Prerequisites

- Android Studio with Android SDK 29 installed
- JDK 17+
- A rooted Android 10 device or emulator for runtime testing

## Open the project

Open the repository root in Android Studio and sync Gradle.

## Build

Use the Android Studio build task or run:

```bash
./gradlew :app:assembleDebug
```

## Notes

The current implementation is a modular scaffold and does not yet contain low-level Android runtime hooks. It is intended as a clean base for implementing Android 10 user lifecycle instrumentation and future interception policies.
