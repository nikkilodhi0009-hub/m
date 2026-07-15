# User Lifecycle Monitoring

The framework is designed to collect detailed evidence for Android 10 multi-user lifecycle transitions.

## Events captured

- user start requests
- user stop requests
- user unlock and switch events
- process creation and death
- package install, uninstall, update, launch, and force stop
- optional binder transaction logging

## Data captured for every lifecycle event

- timestamp
- target user ID
- caller UID / PID
- caller package / process
- binder caller
- Java stack trace
- stop or start reason
- current state transition

## Logging destination

Logs are configured to write to /data/local/tmp/user_stop.log by default.

## Interception policy

The current configuration defaults to observe-only mode. The architecture is prepared to support later policies such as allow, block, delay, modify, and custom-return results.
