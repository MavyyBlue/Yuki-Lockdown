# Yuki Lockdown — Mini Yuki 1.3.0 candidate

The owner-selected Mini Yuki now animates as the Pocket Yuki companion: blinking idle, nearby gait, wave on Boop, waiting/failed reactions and airborne suspension while carried. Tap for Boop/Talk/Move/Hide/Close. Saved position survives restart; optional wandering stays near that anchor. Reduced motion freezes frame playback and stops wandering. Companion still hides during lockouts, keyboard use, device lock and while this Activity is visible.

Owner-supplied final frames are smoothly upscaled2x (192x208 →384x416), retaining identity, alpha and sequence. Resampling does not add new detail. Custom hoodie-grab/eating art and the larger conversational room are next steps, not included here. Original large lockout artwork is unchanged.

Upload **yuki-lockdown-source-v1.3.0.zip** to repository root and replace the existing **.github/workflows/bootstrap.yml** with the matching supplied file. Wait for green build/test/lint AFTER BOTH uploads; install its APK over the current app without clearing data. See PROJECT_HANDOFF/MOBILE_UPLOAD.md. The ZIP also includes the workflow for convenience, but its importer never extracts or commits workflow files.

Baseline HEAD55473d3c0333ef1ef193db63e6547ccb8c5550f2 passed Actions#19. This candidate passes API35 Java/resource checks,25 policy tests and25 installer tests; fresh full CI and phone acceptance remain required.

Rules, persistence, usage reconstruction, browser detection, application ID and signing key are preserved. No new permission/runtime dependency/Internet access. Companion requires connected owner-enabled accessibility service. Talk opens configured ChatGPT; no relay/API integration. Website restrictions rely on supported visible browser address bars, not a firewall. OEM policies and permission revocation may stop enforcement/display. Development signing key is for personal tests, not public distribution.
