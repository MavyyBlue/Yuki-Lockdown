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
