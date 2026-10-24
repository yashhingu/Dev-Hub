# Contributing

Issues and pull requests are welcome. Keep changes focused and describe the user-facing behavior or bug they address.

## Local checks

Use JDK 17 or newer and Android SDK 35. Set `ANDROID_HOME` or create a local `local.properties` file with `sdk.dir`.

```bash
bash gradlew :app:assembleDebug testDebugUnitTest :app:lintDebug
```

On Windows, use `gradlew.bat`. Add or update a test when changing data mapping or repository behavior. CI runs the same commands for every push and pull request.
