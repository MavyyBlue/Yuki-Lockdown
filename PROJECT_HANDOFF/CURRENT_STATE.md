# Current state — 1.1.0 presentation candidate

Authoritative baseline inspected: main c986bd5cd90f9fff7b66fc83567ad68fc5faa89a. Latest inspected successful run: 36623233115. Owner confirmed prior app installed and works, then requested modern UX and a character that appears on screen without a black rectangle.

## This slice

Native UI refresh: bottom navigation, updated cards/fields/buttons/switches, weekday chips, time-picker inputs, bottom-aligned editors, centered wide-screen content. Ui centralizes presentation; NavIcon draws navigation glyphs; Intervention supplies both the full touch-blocking accessibility surface and the labelled preview. GuardService retains the existing rule/attempt/Home/redirect/bypass paths and now delegates only presentation. Rules, Store, UsageLedger, Device and the signing keystore are byte-for-byte unchanged from baseline. Database schema remains1. Presentation-only reduced-motion preference uses private SharedPreferences.

The original approved combined asset ZIP was recovered. Its android_png_768 exports and originals_1254 are RGBA with real alpha; they replace flattened JPEG copies. ImageView background is now transparent. No art generation/editing was required. Asset provenance and exact pixel data are validated during packaging.

Overlay: dimmed home backdrop, edge entrance, approved reaction/profile image above and overlapping a light speech card, three explicit actions. The blocked target is still sent Home first. The full overlay remains touchable; no NOT_TOUCHABLE/NOT_TOUCH_MODAL flags or touch-through areas. Long text and large fonts scroll; artwork shrinks at font scale1.5+ or short height. Reduced motion disables entrance transforms and system animator settings are honored.

## Validation and remaining limits

17 local installer tests (10 original +7 update) passed. Archive validation exercises fresh install, upgrade, repeated run preserving later owner edits, workflow preservation and source-edit conflicts. Six runtime PNGs are RGBA with transparent corners and match approved export bytes. Android instrumentation tests added for alpha decoding and touch-consuming presentation. These device tests have not run yet.

This exact code has not been compiled here. Attempt to create a CI validation branch was denied by GitHub integration403. Full Android build, lint and device APK compilation remain mandatory workflow gates after owner upload; no suppression or bypass was added. Real-device visual/large-text/landscape/keyboard checks are pending. No fabricated app screenshots or certification.

Optional AI relay is deferred. Dialogue remains local and Talk to Yuki opens the configured destination. No Internet permission or new runtime SDKs.

## Baseline architecture / limitations (retained)


Repository baseline inspected: MavyyBlue/Yuki-Lockdown main at c65dc5d5ba094670cfccbe9fea5a0762fb934579, README-only. No implementation has been pushed by this session. User installs through the two-artifact mobile bootstrap.

## Evidence and recovery

A preceding implementation candidate completed assembleDebug, testDebugUnitTest, lintDebug and assembleDebugAndroidTest. A later clean run reported 25 unit tests passing and APK compilation before network/tool interruption. The temporary workspace was subsequently removed. Approved assets and source were recovered from a saved checkpoint; compatibility fixes, tests, docs and packaging were restored. Final package bootstrap tests (10) run locally; archive layout and idempotence are checked at packaging.

The exact final recovered tree has NOT been rebuilt locally. Historical success is not proof that this exact package builds. The workflow requires a fresh successful Android build, unit tests and lint before committing and publishing an APK. No emulator instrumentation run or real-phone acceptance run completed. Do not call this production-certified. User-requested packaging took priority over another lengthy environment download.

## Architecture

Native Java Activity and Android widgets, no runtime dependencies. MainActivity/Ui implement five primary screens, editors, permission explanations, emergency bypass and Yuki styling. Rules is pure deterministic policy; UsageLedger rebuilds per-app foreground durations from UsageStats events. Store is SQLiteOpenHelper schema 1 with atomic JSON configuration, attempt counters and reboot-scoped bypass. GuardService is an owner-enabled accessibility service; BrowserDomains reads only known browser URL fields. Warnings/receivers schedule inexact notifications and replan on restart/time changes.

## Policy

1. Critical safe apps (self, system UI/settings, launcher, phone/SMS, input methods and emergency-related packages).
2. Owner permanent exemptions.
3. Owner pause or active emergency bypass.
4. Union of active scheduled profiles, deterministic strict-first then profile UUID ordering for presentation.
5. Daily allowance/permanent app restriction; permanent domains.
6. Allow.

Schedules override banked allowance. Weekdays attach to the START date. End is exclusive; equal start/end means 24 hours. Overnight period IDs remain stable across midnight; attempts persist per period, prune after eight days and cap at 999; four reaction stages. Daily windows use current local calendar date and zone, including DST. Owner clock/zone changes alter these local-calendar rules; this is not tamper-proof.

Strict schedules increase bypass countdown (at least 60 seconds), not irreversible device control. Rules remain editable. Repeated attempt expression escalation uses approved warning/annoyed states. First intervention uses profile outfit; neutral remains in Home.

## Boundaries

Accessibility cannot be an undefeatable jailer. There may be a brief launch flash. Uninstall, permission revocation, force-stop, safe mode, OEM battery management and other profiles can disable enforcement. Usage access revocation removes reliable allowance measurement; schedules still have their independent time rule. Status/setup communicates missing permissions. Multiwindow/background playback are approximate OS foreground events, not media consumption. Reboots preserve configuration; service reconnect depends on Android/OEM behavior. Uninstalled app rules remain saved harmlessly until edited/reinstalled.

Website matching is supported-browser UI detection, not packet blocking. Navigation may begin before intervention; background traffic, hidden address bars, private/incognito behavior and unsupported browsers are not reliably blocked. No browsing-history database, VPN, remote admin, telemetry or analytics. No INTERNET permission. AI destination opens an external app/browser whose own privacy policy applies.

Warnings are optional inexact alarms; Android Doze/battery policy can delay them. No exact-alarm permission is claimed. Accessibility overlays need no SYSTEM_ALERT_WINDOW permission; owner enables the accessibility service after disclosure. Notification access is requested on Android 13+. Android 13+ sideload restricted-settings safeguards may require owner approval in App info.

Configuration corruption fails open with a visible diagnostic and refuses silent overwrite. There is no automatic destructive reset. Owner can recover via app data management; device tests use disposable data. Database migrations must be implemented before changing the schema version.

## CI repair — 2026-09-29
Run 36618648320 built the application APK and completed the unit-test task, then failed compiling StoreTest because android.test.AndroidTestCase was unavailable. Migrated all five device tests to JUnit4/AndroidX AndroidJUnitRunner with pinned test-only dependencies (runner 1.6.2, ext:junit 1.2.1). No device-test gate was removed. Separate workflow retains the explicit platform-tools SDK fix. The corrected package requires a new green CI run; device execution remains pending.

## API26 theme repair - run 36619520827
CI confirmed 25 unit tests passed, app APK built, and AndroidX device-test Java compilation passed. Lint reported one error: windowLightNavigationBar requires API27 but minSdk is26. Removed the unnecessary false-valued default theme item. All previous SDK and AndroidX fixes are preserved byte-for-byte. No checks disabled. New green CI still required.

## UI build repair - run 36633797483
App and device-test Java compilation succeeded; all 25 unit tests passed. Full lint report contained one error (WrongConstant): raw font-style 0. Replaced only that expression with Typeface.NORMAL. No lint checks disabled; complete corrected CI run still pending.
