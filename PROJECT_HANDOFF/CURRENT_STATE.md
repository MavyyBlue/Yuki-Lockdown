# Current state — Mini Yuki 1.3.0 candidate

Live main inspected: 55473d3c0333ef1ef193db63e6547ccb8c5550f2. Latest Actions run #19 /36962697324 succeeded on that SHA: build, unit tests, lint and test-APK compilation; emulator execution skipped. This supersedes the stale pending-build claims for 1.2. Phone acceptance has not been reported in this chat.

## This slice

Owner selected Mini-Yuki-Creation.zip as the companion and requested smoother scaling. Nine animation rows provide 57 final transparent 192x208 frames. Each is deterministically resampled with Mitchell filtering to 384x416, with no new artwork/detail. Original source atlas and manifest are preserved; 16 look directions are retained there for future integration. No ChatGPT Work pet was modified or activated by this Android integration.

MiniYukiView replaces only the companion's static ImageView. Native frame playback shows blink/idle; nearby roaming uses supplied left/right gait loops; Boop waves; schedule warnings use waiting; return after lockout uses failed. Carry freezes the existing airborne jump frame with suspended sway, not a custom hoodie-grab pose. Reduced motion/system animation-off freeze playback and stop movement. A new settings toggle disables wandering. The companion wanders by at most24dp on either side of the saved anchor, approximately every30 seconds; roaming never writes saved placement. Dragging saves placement. Tapping cancels roaming before opening its menu. Hiding removes the view and cancels frame/movement callbacks; lockout/app/keyboard/lock-screen behavior remains as in1.2.

All existing boundary policy/storage/usage/browser code, original six blocking-surface images, application ID and signing key are unchanged. VersionCode4. Existing five-area app UI remains; the larger conversational room has not been implemented. Proposed layout boards remain unapproved. Feed-data deletion/eating and custom hoodie pose remain future work.

## Validation

Independent API35 resource compile/link and main Java compilation pass. Existing25 policy tests and25 installer tests pass.57 runtime PNGs are RGBA384x416 with transparent corners; frame counts and updated manifest hashes checked. Exact source ZIP upgrade against inspected baseline is simulated; workflow preservation, rerun idempotence and independent-source edit refusal are checked.

Full Gradle/lint and instrumentation for THIS candidate remain pending: this environment previously failed to resolve AGP8.9.2. No APK is presented as certified. The matching manually uploaded workflow keeps full build/test/lint and device-test compilation gates, with optional emulator dispatch. Real-phone tests must cover sprite timing, drag/menu interactions, saved placement, lockout hide/restore, large text, reduced motion, orientation and service lifecycle.

## Upload

Deliver yuki-lockdown-source-v1.3.0.zip and matching bootstrap.yml separately as requested. Replace the EXISTING .github/workflows/bootstrap.yml and upload the intact ZIP at repository root. Installer pins a canonical payload digest, guards all destination hashes using tools/mini_baseline.json, and skips the bundled workflow. It never writes or commits workflows. It requires the existing bootstrapped app. Subsequent runs build expanded source without restoring archive. See MOBILE_UPLOAD.md.
