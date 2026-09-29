# Yuki Lockdown

Android-first, local personal digital boundaries. Owner-chosen app allowances, permanent exemptions, scheduled app/domain restrictions, approved Yuki interventions, and deliberate emergency bypass. Android 8+ (API 26), target API 35. This is a real-device acceptance candidate, not a certified production release.

## Install entirely from your phone

1. Upload **yuki-lockdown-source-v1.0.0.zip** unchanged to the repository root.
2. Upload the separately supplied **bootstrap.yml** to **.github/workflows/bootstrap.yml** on the same branch. The ZIP intentionally contains no workflow files.
3. Open Actions → **Yuki Lockdown • Bootstrap & Android**. Uploading triggers it; you can also choose Run workflow on main (manual dispatch requires the workflow on the default branch).
4. Initial run checks the pinned ZIP hash and safe paths, expands the source, runs bootstrap tests, compiles the APK, runs Java unit tests and Android lint, then commits **Bootstrap Yuki Lockdown v1.0.0 [skip ci]** and pushes to the active branch. It preserves the workflow and ZIP. No other workflows are written.
5. Inspect the **green run**, its **Verified source commit** summary, and that commit's expanded files. Download the **yuki-lockdown-debug-APK** artifact. Unzip it on your phone and install **app-debug.apk**; VERIFIED_SOURCE_COMMIT.txt identifies the code used.
6. Open Yuki → Settings, read permission disclosures, enable accessibility and usage access, and optionally notifications. Set ChatGPT/your AI apps to Always Allowed. Follow PROJECT_HANDOFF/PHONE_ACCEPTANCE.md.

The APK is uploaded only after successful validation and push. If push is denied, allow Actions read/write contents in repository settings or use a branch where direct commits are permitted, then rerun. Never force-push. The initial repository must contain only README.md, the source ZIP, and .github (plus Git metadata); existing source is refused to prevent accidental overwrites.

After bootstrap, **expanded source is authoritative**. A marker makes later runs ignore the ZIP and build current source without generating another commit. Do not delete the marker to apply updates. Make ordinary source commits. The workflow's own commit uses [skip ci]; normal subsequent source pushes build/test. Re-running unchanged source makes no commit.

## Implemented

Home status/usage/upcoming schedule; installed app selector with metadata/icons; allowances and exemptions; domain list; arbitrary schedule profiles with weekdays, overnight windows, app/domain selection, Yuki mode, warnings, strictness and enable toggle; owner settings and diagnostics; approved character overlay with Home/AI redirect; persistent reaction state; emergency countdown and reason; SQLite persistence; boot/time-change warning replanning.

Website restrictions use local accessibility inspection of supported browser address bars, **not a VPN or DNS firewall**. Supported adapters: Chrome, Brave, Edge, Samsung Internet, Firefox. Hidden bars, changed browser UI, private modes, embedded webviews and other browsers may escape detection. An exempt browser bypasses domain rules. No universal network enforcement is claimed.

## Build and validation

JDK 17, Android SDK 35/build-tools 35.0.0, Python 3, curl/unzip/sha256sum. `./gradlew assembleDebug testDebugUnitTest lintDebug`; `python3 tools/test_bootstrap.py`. Windows: `gradlew.bat`. The small launchers download official Gradle 8.11.1 and verify its pinned SHA-256; they do not need a wrapper JAR. AGP 8.9.2. First build requires network for build dependencies. Runtime has no Internet permission or external SDKs.

See PROJECT_HANDOFF/CURRENT_STATE.md for precise verification evidence and limits. Historical development candidate built successfully and 25 unit tests passed before workspace loss. This recovered/repackaged exact candidate has not been rebuilt locally; the supplied Action is the required build gate. Instrumentation and phone tests remain pending.

Public development signing key in tools/development.keystore enables consistent test updates. It is intentionally NOT a secret or production signing identity (alias androiddebugkey, password android). A separately secured private signing key and release review are required before distribution. Do not enter personal secrets into the repository.

## Documentation

- PROJECT_HANDOFF/CURRENT_STATE.md — architecture and present evidence
- PROJECT_HANDOFF/DEVELOPMENT.md — build, permissions, persistence and maintenance
- PROJECT_HANDOFF/PHONE_ACCEPTANCE.md — actual device checks
- PROJECT_HANDOFF/ROADMAP.md — remaining release work
- PROJECT_HANDOFF/CHANGELOG.md — implementation history

Approved source assets remain under assets/approved; runtime exports under app/src/main/res. Character PNGs supplied with solid black backgrounds are preserved, not artificially cut out. No replacement Yuki artwork was generated.
