# Mobile upload — source ZIP and matching workflow

1. Download **yuki-lockdown-source-v1.3.0.zip** and keep the original ZIP intact. Extract a copy in your phone file manager (enable hidden-file display if needed).
2. Download the separately supplied bootstrap.yml (identical to the bundled copy). In GitHub, replace the existing **.github/workflows/bootstrap.yml** with the file at that same path in the extracted ZIP. Use Upload files in that workflow folder, or open the existing file, Edit, and paste the bundled file text. Commit normally to main. Do not create another workflow.
3. Upload the ORIGINAL **yuki-lockdown-source-v1.3.0.zip** to the repository root. Do not rename it or rezip it. These two uploads can be done in either order; an intermediate run can fail or build the old app. Wait for the run AFTER BOTH updates.
4. Open Actions → Yuki Lockdown • Bootstrap & Android. Require green build, unit tests, lint and device-test APK compilation. The workflow commits the imported source only after these pass, without modifying workflows. Inspect its Verified source commit.
5. Download **yuki-lockdown-debug-APK**, extract app-debug.apk and install over your existing app. Keep the app installed and keep its data. This uses the same application ID/development signing key, versionCode4.
6. Enable/retain the Guard accessibility service. Pocket Yuki appears after leaving the app. In Settings, Pocket Yuki companion can be disabled or her position reset.

The original importer cannot install another ZIP by itself: it pins the old package and ignores ZIP changes after installation. That is why step2 is necessary, even though replacing only the ZIP might seem sufficient. If an update fails due to owner/source edits, do not delete the bootstrap marker or force overwrite; return the failure log for a fresh baseline package.

## First phone checks

- Leave the app: one tiny transparent Yuki appears. Tap opens actions. Close removes only the menu. Apps outside her small rectangle remain usable.
- Move → drag → release, including near edges. Rotate, reopen app and reconnect Guard: position stays reachable. Reset restores default position.
- Boop reacts. Talk opens configured ChatGPT destination. Hide stays hidden; Settings switch restores her. Reduced motion stops sway.
- Trigger a real restriction: companion/menu disappear before the full blocking surface. Dismiss via Return Home: exactly one companion returns at her prior position. Real overlay still blocks touches, including transparent regions.
- Repeat warning/lockout attempts: existing counters/policy remain unchanged. Scheduled warning selects warning artwork briefly.
- Open keyboard, lock/unlock screen, disconnect/reconnect Guard, increase fonts and use landscape/TalkBack: actions remain reachable, no orphan overlays.
- Existing rules/exemptions and in-place installation survive. Device tests must use disposable rules, never personal data.

This version adds real Mini-Yuki blink/gait/wave/reaction frames and nearby roaming. Settings → Let Yuki wander nearby disables roaming. Custom hoodie-grab/eating animation, feed-data deletion and the larger Yuki room remain future slices. Verify Carry shows the existing airborne frame, and reduced motion freezes frame playback and roaming.
