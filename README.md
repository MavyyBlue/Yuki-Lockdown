# Yuki Lockdown — Pocket Yuki 1.2.0 candidate

Android-first, local digital boundaries with an optional tiny Yuki companion. Tap her for Boop, Talk, Move, Hide and Close. Move arms dragging with suspended sway; release remembers position. Idle motion uses approved transparent art. She hides during lockouts, keyboard use, screen lock and while the app is open. Settings provides companion visibility, reset position and reduced motion.

Download/upload instructions: **PROJECT_HANDOFF/MOBILE_UPLOAD.md**. One source ZIP contains everything, including the replacement for the existing workflow; no separate YML download. The workflow must still be uploaded manually because the old importer ignores new archives. New importer never commits workflow files.

Existing limits, schedules, websites, exemptions, emergency bypass, database schema, application ID and signing identity are preserved. No new permission or Internet access. Accessibility service connection is required for Pocket Yuki. Talk opens the configured external ChatGPT destination; no ChatGPT API/relay is included.

Current baseline: 4b749207551093a3945c32d7db5810fb1781090a, validated by Actions run #16. This candidate passes independent API35 Java/resource compilation, 25 policy tests and 21 installer tests. Full Gradle build/lint failed to start locally due to AGP dependency resolution; fresh green CI and phone acceptance are required. See CURRENT_STATE.md for evidence and limitations.

Walking, data feeding, custom animation poses and the character-centered room are later slices. Current idle/carry animation transforms static approved images. Proposed UI boards remain unapproved.

Website coverage uses supported browser address bars, not a VPN/firewall. OEM battery policies, force-stop and permission loss can interrupt enforcement and companion display. All data remains local. Public development key is for personal testing, not public production releases.
