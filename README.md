# Android 10 Hooking Framework

This repository contains a fresh, modular Android 10 framework scaffold inspired by LSPosed-style architecture concepts without copying upstream source code.

## What is included

- A multi-module Android Studio project layout for app, core, API, common, hooks, manager, service, bridge, daemon, and example-module.
- A sample Android app entry point that initializes the framework at startup.
- A reusable logging and lifecycle model for user-start and user-stop monitoring.
- Documentation for architecture, lifecycle monitoring, and getting started.
- Configuration files for framework, hook, and logging behavior.

## Key focus

The project is structured around the research goal of investigating Android 10 multi-user lifecycle transitions, especially user stop detection and the full execution path from caller to shutdown.

## Verification status

- Editor diagnostics for the main Kotlin files report no issues.
- A Gradle verification attempt was made with ./gradlew -q help, but this environment does not currently expose an Android SDK, so a full Android build could not be completed here.

## Next steps

1. Open the repository in Android Studio.
2. Install or point Android SDK 29 to the local environment.
3. Sync Gradle and build the debug app.
4. Extend the hook engine with real Android runtime instrumentation on a rooted Android 10 device.
