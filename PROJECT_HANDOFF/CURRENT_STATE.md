# Current state — Pocket Yuki 1.2.0 candidate

Inspected live main: 4b749207551093a3945c32d7db5810fb1781090a. Actions run #16 (36635119525) passed Android build, unit tests, lint and test-APK compilation and committed that HEAD. Emulator execution was skipped. Previous docs stating the 1.1 build was pending were stale. Phone acceptance is still pending.

## Implemented slice

PocketYuki is a small accessibility overlay owned by the existing GuardService, enabled by default and switchable in Settings. Approved neutral artwork has procedural idle sway/breathing. Tap opens a small scrollable hovering menu: Boop, Talk, Move, Hide, Close. Move arms dragging with a suspended sway; release saves normalized position. Cancel restores the starting point. Bounds are clamped on display changes; a settings action resets position. Reduced motion and the system animator switch are honored. No new permission, runtime dependency, network access or rule authority is added.

The companion hides before the real blocking surface is attached and returns on the next eligible service tick after dismissal. It also hides when the keyboard is visible, the device is asleep/locked, this Activity is open, or system setup/permission surfaces are detected. Hiding removes both menu and character windows and cancels animation. Guard disconnect/destroy removes the windows. Warning delivery briefly selects warning artwork; restrictions select the existing outfit/escalation artwork. Position survives app/process restart via private preferences, independently of rule storage.

The small companion window consumes its own rectangle only; the real intervention remains full-size and touch-blocking. No companion view is inserted into policy evaluation. Rules, Store, Device, UsageLedger, browser detection, application ID and development key remain unchanged. VersionCode 3 supports an in-place upgrade.

## Scope and limitations

This is the agreed first companion slice. Motion transforms animate approved static PNGs; they are not a drawn walk cycle, blinking facial rig or custom hoodie-grab pose. Feed-data deletion, walking, expressive animation assets and the larger character-centered room are not implemented. No disabled fake feed action is presented. The six proposed layout boards remain unapproved. Existing 24-hour equal-time schedule behavior is preserved.

Companion persistence depends on the owner-enabled accessibility service. Android/OEM power management and permission revocation can stop it. It is not guaranteed over every system/protected screen. Dragging, inset behavior, keyboard hiding, TalkBack and OEM lifecycle require phone tests.

## Validation

Local: all main Java sources compile against API35 using Eclipse ECJ with the Android namespace JAR; Android resources compile and link with aapt2; 25 existing policy/usage unit tests pass; 21 installer safety tests pass. Exact packaged update is simulated against baseline with idempotence, owner-edit refusal and workflow preservation.

Full Gradle attempt failed before compilation because AGP 8.9.2 could not resolve in this environment. These independent checks do not certify full Gradle build, lint, DEX packaging or instrumentation. A fresh green GitHub run on the imported source and real-phone checks are required before installation acceptance.

## One-ZIP update

The old workflow pins version1.1 and ignores replacement ZIPs after its marker. This single source ZIP includes a replacement for the EXISTING .github/workflows/bootstrap.yml. The owner extracts and uploads that file manually, then uploads the original ZIP at the root. It is not a second workflow. The updater verifies a pinned canonical source-payload digest, validates all paths and baseline destination hashes before writes, and never extracts or commits .github files. The workflow is excluded from the payload hash to avoid a self-referential ZIP checksum. Subsequent runs build expanded source without restoring the archive. See MOBILE_UPLOAD.md.
