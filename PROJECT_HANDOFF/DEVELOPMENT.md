# Development

## Reproduce

Install JDK 17 and Android SDK platform 35/build-tools 35.0.0. Set ANDROID_HOME or local.properties sdk.dir. Run `./gradlew assembleDebug testDebugUnitTest lintDebug assembleDebugAndroidTest`. Outputs: app/build/outputs/apk/debug/app-debug.apk, app/build/reports/tests, app/build/reports/lint-results-debug.html. Python bootstrap tests: `python3 tools/test_bootstrap.py`.

Gradle launcher is a small checksum-pinned downloader, not the standard Java wrapper. It requires curl, unzip and sha256sum on Unix; Windows launcher uses PowerShell. Distribution 8.11.1 SHA-256: f397b287023acdba1e9f6fc5ea72d22dd63669d59ed4a289a29b1a76eee151c6. Downloads/dependencies require network only during build. Native app has no network permission.

Instrumentation: disposable API 26+ emulator/device, `./gradlew connectedDebugAndroidTest`. StoreTest intentionally clears this app's config/attempt/bypass tables; DO NOT run it against personal rules. Optional workflow dispatch device_tests runs API35 emulator. Unit tests cover hierarchy, exact boundaries, crossing midnight, 24-hour profiles, DST reset, overlapping schedules, domains, reactions and usage reconstruction. Instrumentation covers persistence, corruption, counters, bypass and permission-denied handling.

## Permission model

Accessibility service: detect foreground packages, inspect known URL bar nodes locally, return Home and show TYPE_ACCESSIBILITY_OVERLAY. Owner explicitly enables it; this is not an accessibility aid. No overlay special permission is needed. UsageStats: foreground duration reconstruction, read only after owner grants Usage access. Notifications: advance warnings and status, optional. Boot completed: replan warning alarms. No device administrator, VPN, exact alarm, all-packages query, Internet or background surveillance permission.

## Data

SQLite yuki.db schema1: config JSON version1 (single atomic replace), attempts (period/count/time), bypass (elapsed deadline + boot count). Application context singleton. No browsing history or raw usage event storage. Usage is reconstructed from Android system events, clipped to local midnight. Process death does not reset configured allowances; daily totals come from the OS. Multiwindow may count multiple resumed apps. Unsupported/missing OS events may undercount. Bypass uses monotonic elapsed time and expires across reboot.

Configuration validation rejects invalid numeric ranges, duplicate IDs and unknown schema version. Corrupt data pauses protection, preserves database, and exposes an error. Retain this fail-open behavior in migrations. Exemptions must remain ahead of schedules. All dates use device local zone; manual clock changes intentionally follow owner's clock.

## Assets

assets/approved contains original supplied PNG/SVG files. app/src/main/res/drawable-nodpi contains scaled Android PNG exports, without density enlargement; drawable contains native vectors. Character PNGs retain their approved transparent alpha. No artwork has been redesigned. UI text/buttons remain native and separate. App icon uses supplied adaptive layers.

## Bootstrap security

Standalone manually uploaded workflow embeds tools/bootstrap_extract.py. ZIP SHA is pinned in workflow. Initial extraction allows only known source roots, rejects .github/.git paths, traversal, symlinks, duplicate/case-colliding entries and nested ZIPs. Size/count bounds and CRC validation precede staging. Existing non-bootstrap source makes initial installation fail. Marker makes reruns build authoritative source instead of restoring ZIP. Build/test must pass before commit/push. No force push; branch protection may require a permitted branch. Workflow modifications are explicitly refused before commit. Bootstrap own commit uses [skip ci].

If interrupted before commit, GitHub's next run starts from clean checkout. Local partial extraction is refused on rerun instead of overwritten. To upgrade, edit expanded source normally; do not alter marker or replace ZIP expecting an upgrade. Keep bootstrap installer and embedded workflow copy aligned if intentionally revising the bootstrap system.

## Release

The included public development key is only for personal testing; secure separate release signing is mandatory for public distribution. No Play listing/privacy review or store accessibility policy approval is included. Test target SDK upgrades, OEM service lifecycle, supported URL bar IDs, font scaling and accessibility before release.

Device tests now use AndroidX Test (test APK only), not the removed legacy android.test API. The application runtime still has no third-party dependencies.

## 1.1 UI and update maintenance
Presentation lives in Ui, NavIcon and Intervention. Preview uses the same renderer without invoking rule evaluation or attempt persistence. Keep full-size touch handling in the real accessibility overlay even if the backdrop is made more transparent. Respect system animator settings and private presentation/reduce_motion. Runtime PNGs must retain alpha and remain drawable-nodpi.

The 1.1 standalone workflow embeds tools/install_update.py; tools/update_baseline.json pins source blob hashes at the inspected main commit. Before any writes it checks all archive entries and all destination hashes. It allows only baseline bytes or exact target bytes; it refuses independent source edits. After success its marker causes reruns to ignore the package. Do not delete the marker or repurpose this package to overwrite newer work. The historical 1.1 ZIP contains no workflow. The 1.2 ZIP contains a manually uploaded replacement workflow; its importer explicitly skips that one entry and never writes or commits workflow files.

## Pocket Yuki 1.2
PocketYuki is owned by GuardService and stores only enable/normalized position in pocket_yuki preferences. It does not evaluate restrictions or modify config. Menu and companion windows are small and not focusable; explicit Close is always available. Move arms the next drag. Procedural transform motion honors reduced-motion/system animator preferences. User-owned rules and SQLite schema remain unchanged.

Run python3 tools/test_pocket_update.py for the new update guard. tools/pocket_baseline.json pins inspected HEAD file hashes. The replacement workflow embeds install_pocket_update.py and a canonical payload hash (sorted filename + NUL + SHA256 bytes, excluding the bundled workflow). It validates all entries and all destinations before writing, skips workflow extraction and retains idempotence after successful installation.

## Mini Yuki1.3
MiniYukiView uses AnimationDrawable with filtered BitmapDrawables from owner-supplied2x frames. Attach starts playback; detach stops drawable scheduling. State changes avoid rebuilding unchanged loops. PocketYuki bounds optional roaming near the saved anchor; taps/drags/hide stop movement. Reduced motion freezes both frames and transforms. See assets/approved/mini_yuki for source/upscale provenance. The new importer/install_mini_update.py uses tools/mini_baseline.json and tools/test_mini_update.py, with the same guarded/idempotent update behavior.
