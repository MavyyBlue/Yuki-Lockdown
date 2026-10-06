# Development — owner control update

Use JDK17, Gradle8.11.1, Android SDK35 and build-tools35.0.0. Run `./gradlew --no-daemon :app:testDebugUnitTest :app:assembleDebug :app:lintDebug :app:assembleDebugAndroidTest` and `python3 -m unittest discover -s tools -p 'test_*.py'`. Current full local Gradle checks pass; earlier offline-plugin-resolution notes are historical.

Preserve tools/development.keystore and its original certificate. SQLite1→2 is additive. Exported bridge calls must authenticate Binder UID/package/certificate and owner opt-in before parsing; internal dispatcher tests are not real signed-Binder device proof. Keep expected-state compare-and-set, durable mutation claims/receipts, safe-app and bypass countdown invariants.

The standalone guarded control importer is embedded identically in bootstrap.yml. Updating mobile source requires its exact payload hash and tools/control_baseline.json guard. Do not replace owner edits or overwrite workflow files via a ZIP. Require exact recorded CI source before declaring CI verification, and separate Galaxy owner acceptance.
