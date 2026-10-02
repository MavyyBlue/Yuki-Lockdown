# Mobile upload — source ZIP and matching workflow

1. Download **yuki-lockdown-source-v1.5.0.zip** and keep the original ZIP intact. Extract a copy in your phone file manager (enable hidden-file display if needed).
2. Download the separately supplied bootstrap.yml (identical to the bundled copy). In GitHub, replace the existing **.github/workflows/bootstrap.yml** with the file at that same path in the extracted ZIP. Use Upload files in that workflow folder, or open the existing file, Edit, and paste the bundled file text. Commit normally to main. Do not create another workflow.
3. Upload the ORIGINAL **yuki-lockdown-source-v1.5.0.zip** to the repository root. Do not rename it or rezip it. These two uploads can be done in either order; an intermediate run can fail or build the old app. Wait for the run AFTER BOTH updates.
4. Open Actions → Yuki Lockdown • Bootstrap & Android. Require green build, unit tests, lint and device-test APK compilation. The workflow commits the imported source only after these pass, without modifying workflows. Inspect its Verified source commit.
5. Download **yuki-lockdown-debug-APK**, extract app-debug.apk and install over your existing app. Keep the app installed and keep its data. This uses the same application ID/development signing key, versionCode7.
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

This version uses adult companion sprites. Settings → Floating Yuki size changes the overlay height from80–240dp and saves it. After setting a size, leave the app to see the result. Home has its own 240–400dp responsive adult stage; the slider changes only the floating companion.

- Test the smallest/largest size, drag to edges, rotate and restart: the saved size/position remain reachable.
- Boop must show fingers to lips → blowing a kiss/heart → return to idle, with no waving.
- Move must show the hood pulled upward and feet dangling throughout drag; releasing returns to idle.
- Reduce motion: Boop shows a still kiss pose and carry a still suspended pose; wandering stops.
- Check lockout hide/restore at both size limits; no duplicate companion or leftover menu.

Full Android build/lint and real-device approval must come from the fresh run and phone checks for this version. Browser interaction and layout-board approval remain outstanding.

## 1.4.1 idle/wander checks

- Close the companion menu and watch20–30 seconds. The body/feet should stay still while idle, with a brief blink; then the whole overlay walks to another position and rests.
- At either screen edge, enable wandering: it should travel inward, never loop its gait without travel. Turning wandering off keeps her at her current position with steady idle.
- Try both size limits, Move/drag, Boop and a real lockout: kiss/carry, saved size/manual position and hide/restore must still work.
- Reduce motion stops roaming and blink playback. Menu open pauses wandering.

Baseline1.4.1 passed Actions run25; this1.5.0 still needs a fresh green run and owner phone check. Optional Actions manual dispatch with device_tests=true runs the added fixed-body rendering regression on a disposable emulator.

## First 1.5.0 Home check
Tap adult Yuki → choose Settings, Protected apps and Restricted domains in turn → return Home. Close/Back must dismiss the dialogue. Check large text/landscape scrolling, reduced motion, background/restore and retained floating settings. Follow PHONE_ACCEPTANCE.md for the full candidate checklist.
