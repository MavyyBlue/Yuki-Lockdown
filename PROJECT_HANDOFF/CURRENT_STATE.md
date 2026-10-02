# Current state — companion Home 1.5.0 candidate

## Verified baseline

Inspected live main HEAD: **4c08a574dbba5d9c1409a1a6cdb7197266b5800e**. Successful Actions **#25 / 36968675697**, android job **110717867497**, imported 1.4.1/versionCode 6, passed APK build, unit tests, lint and device-test APK compilation, then pushed this exact SHA. Job logs explicitly show commit/import and successful build. Emulator execution was skipped. This supersedes the old pending 1.4.1 build notes. Owner likes the latest companion; broader real-device/browser checklist remains incomplete.

## Bounded slice

HomeYuki replaces Home's static original neutral portrait with the existing mature adult MiniYukiView renderer. No asset regeneration. Steady blinking uses the same fixed-body IdleYukiDrawable as the floating companion. The stage follows its measured width, clamped to 240–400 dp height; the existing scrolling shell remains. In-app scale is independent of floating size preferences.

Tap Yuki or Talk with Yuki to open one centered native dialogue card with Settings, Protected apps, Restricted domains and Close. Those choices use MainActivity's existing Settings/Apps/Websites routes and editors. No rule writes occur from merely opening/closing the card or choosing a destination. Bottom navigation and original setup/status/usage/schedule/bypass features remain. Separate Talk to Yuki still uses Device.talk; no AI relay/API integration is added.

Dialog is dismissed before navigation, on pause, on detach and before rerender. Home's animation stops on pause. Asynchronous usage refresh does not rerender while paused or while choices are open; a later Home render uses the latest measurements. Reduced motion and Android animator-off use existing still rendering. No new preferences, permissions, schema or dependencies.

VersionCode 7 / versionName 1.5.0. Policy, Store, UsageLedger, Device, BrowserDomains, GuardService, PocketYuki, manifest, companion artwork, original six intervention images, icon/wordmark and signing key are byte-identical to baseline. Saved floating size, manual placement, roaming, kiss, carry and lockout hide/restore remain on the existing path.

## Validation and delivery

Independent API 35 resource compile/link and Java 17 main-source compilation pass. 34 Java unit tests and 37 Python importer tests pass. Included HomeYukiTest covers all three destinations, duplicate menu prevention, Close and pause dismissal on a disposable device; NOT executed here. Local Gradle is blocked by uncached Android plugin resolution in offline mode. Fresh full Actions build/lint/device-test compilation, device execution and phone checks are required before acceptance.

Guarded installer/test: tools/install_home_update.py and tools/test_home_update.py. tools/home_baseline.json pins inspected HEAD blob hashes. Source ZIP upgrade simulation verifies CRC/checksum, exact baseline installation, workflow preservation, idempotence and policy/storage/art/signing identity. The importer skips the bundled workflow; owner manually replaces the same .github/workflows/bootstrap.yml. Deliver one yuki-lockdown-source-v1.5.0.zip and identical separate bootstrap.yml. Historical unused mini assets are omitted from the mobile archive and remain untouched in the repository.

Original layout boards still await explicit approval. This is a functional Home entry slice, not approval of those boards. Feed-data deletion remains unimplemented; no eating/deletion action is exposed. Larger room scenery, richer dialogue/reactions and optional AI conversation remain future work.
