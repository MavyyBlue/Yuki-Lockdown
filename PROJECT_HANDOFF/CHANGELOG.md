# Changelog

## 1.0.0 — 2026-09-29, phone-test candidate
- Native five-area Android interface and approved Yuki asset integration.
- Deterministic policy, daily usage reconstruction, profile schedules, exemptions and domain matching.
- Accessibility intervention, AI redirect, persistent reactions, owner bypass and optional warnings.
- SQLite configuration, corruption fail-open behavior, reboot/time-change receivers and diagnostics.
- Java policy tests, device persistence tests, safe mobile bootstrap installer and test suite.
- Recovered source from saved checkpoint after transient workspace removal; restored fixes and packaging.
- Exact recovered build awaits GitHub validation and real-device acceptance; historical candidate build evidence is explicitly distinguished.

## CI repair — 2026-09-29
Run 36618648320 built the application APK and completed the unit-test task, then failed compiling StoreTest because android.test.AndroidTestCase was unavailable. Migrated all five device tests to JUnit4/AndroidX AndroidJUnitRunner with pinned test-only dependencies (runner 1.6.2, ext:junit 1.2.1). No device-test gate was removed. Separate workflow retains the explicit platform-tools SDK fix. The corrected package requires a new green CI run; device execution remains pending.

## API26 theme repair - run 36619520827
CI confirmed 25 unit tests passed, app APK built, and AndroidX device-test Java compilation passed. Lint reported one error: windowLightNavigationBar requires API27 but minSdk is26. Removed the unnecessary false-valued default theme item. All previous SDK and AndroidX fixes are preserved byte-for-byte. No checks disabled. New green CI still required.
