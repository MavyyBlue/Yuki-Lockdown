# 1.6.0 — one-room visual-novel UX candidate

- Room + adult animated Yuki replaces dashboard/header/bottom navigation. No visible fresh-launch controls; tap Yuki, advance greeting, choose Talk / Protected Apps / Sites / Plans.
- Embedded bottom panels/editors/removal confirmation; fixed top-right × validates, saves and returns to dialogue. Errors retain drafts; Discard remains available.
- Staged settings/floating preferences, ordinary draft/flow rotation snapshots, keyboard/inset-aware panel sizing and back/pause handling.
- Existing permission/setup, usage, bypass, schedules/exemptions, AI destination and floating behavior retained; no feed deletion or AI relay.
- Original generated cozy blue night room; all approved Yuki/runtime artwork unchanged. VersionCode8; policy/storage/manifest/signing unchanged.
- Baseline 1.5.0 imported/validated by green Actions #27 (a52e7e4). Candidate: 42 Java + 41 importer tests and SDK resource/source checks pass locally; full Actions/device/phone acceptance pending.

# 1.5.0 — companion Home candidate

- Prominent mature animated Yuki on Home; tap or Talk with Yuki opens floating Settings / Protected apps / Restricted domains choices.
- Reuses existing pages/editors and steady blink renderer; dialogue dismissal and activity animation lifecycle handled.
- Independent in-app stage sizing; saved floating companion settings remain unchanged.
- VersionCode 7; artwork, rules/storage, app identity/signing and Guard/Pocket behavior preserved.
- Baseline 1.4.1 certified for build/test/lint by Actions #25, imported HEAD 4c08a574. 1.5.0 Actions #27 verified; owner requested a one-room UX instead of the dashboard. Device/phone acceptance incomplete.
- Local resource/Java checks, 34 Java unit tests, 37 importer tests and exact package upgrade simulation pass. Device navigation/lifecycle tests included, not executed.

# 1.4.1 — steady idle and visible travel

- Reuse one body during idle; brief eyelid compositing replaces mismatched whole-body frames.
- Wandering moves96dp around saved placement, chooses inward routes at edges and rests12s after arriving.
- Existing kiss, carry, size, policy/save/signing preserved. Baseline1.4.0 green23 verified; 1.4.1 CI verified by run #25; broader phone check pending.

# 1.4.0 candidate

- Adult companion redesign; one-shot blowing kiss on Boop; dedicated hoodie-carry loop.
- In-app80–240dp floating companion size setting, saved and clamped to viewport.
- Rules/storage/signing/permissions and original intervention artwork preserved.
- Baseline successful Actions run21 verified; fresh1.4 CI/phone acceptance pending.

# Changelog

## 1.3.0 — owner-selected animated Mini Yuki
- 57 supplied frames smoothly resampled2x without redraw; native blink/gait/wave/reaction playback.
- Bounded nearby roaming with owner toggle; saved anchor and drag lifecycle preserved.
- Existing jump frame for suspended carry; custom hoodie-grab pose pending.
- VersionCode4, unchanged rule/storage/signing identity; fresh CI/phone acceptance required.
- Baseline1.2 validated by green Actions#19; stale pending-build docs corrected.


## 1.2.0 — Pocket Yuki candidate
- Tiny service-owned companion, idle/suspended motion, floating Boop/Talk/Move/Hide menu.
- Saved normalized position, bounded dragging, reset and reduced motion.
- Hide/restore around interventions, keyboard, screen lock and app foreground.
- Existing warning and lockout artwork reactions; policy/storage/signing unchanged.
- Single archive contains a manually uploaded replacement of the existing workflow.
- Independent Java/resource compile and 25 policy +21 installer tests pass; full CI/phone acceptance pending.


## 1.1.0 — modern presentation and transparent Yuki
- Native bottom navigation, rounded controls, usage presentation and permission-aware Home.
- Transparent original approved character exports; remove ImageView black background.
- Floating character entrance/speech bubble, full touch-blocking overlay, preview and reduced motion.
- Native time picker inputs, weekday chips, bottom-aligned rule forms.
- VersionCode2; preserve application ID, SQLite rules and existing development signing key.
- Safe full-source upgrade package with per-file baseline guards and idempotence.
- Local packaging checks pass; exact Android build and phone acceptance pending.


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

## UI build repair - run 36633797483
App and device-test Java compilation succeeded; all 25 unit tests passed. Full lint report contained one error (WrongConstant): raw font-style 0. Replaced only that expression with Typeface.NORMAL. No lint checks disabled; complete corrected CI run still pending.
