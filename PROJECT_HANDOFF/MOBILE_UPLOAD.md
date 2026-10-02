# Mobile upload — context companion 1.7.0

1. Download **yuki-lockdown-source-v1.7.0.zip** and the matching separate **bootstrap.yml**. Keep the original ZIP intact; don't rename/recompress it. Extract a COPY only if you need the bundled workflow text.
2. Upload the ORIGINAL ZIP to the root of MavyyBlue/Yuki-Lockdown on main.
3. Replace the existing **.github/workflows/bootstrap.yml** with the separate matching file (identical to the bundled workflow). If the phone adds a filename suffix, rename it to bootstrap.yml. Use Upload files inside that folder, or edit the existing file and paste the full matching workflow text. Do not create a second workflow.
4. These two updates can be uploaded in either order. An intermediate run may fail or build old source. Require the green run AFTER BOTH updates: APK build, unit tests, lint and device-test APK compilation. The guarded importer commits/pushes expanded source only after validation; it never modifies workflows. Inspect Verified source commit.
5. Download **yuki-lockdown-debug-APK**, unzip, install app-debug.apk over the existing app and retain its data. Same application ID/development signing key; versionCode9. Do not uninstall first.
6. Check enlarged centered waist-up room Yuki, native TikTok/YouTube beanbag (stationary/inward/kicks), native ChatGPT phone/blush/glow, Move/drag/Boop and reduced motion. Follow the new context checklist in PHONE_ACCEPTANCE.md. Also recheck: launch into the button-free room, tap Yuki, advance greeting, try all four choices. Settings & setup is inside Talk. Change a test setting, close via top-right ×, reopen and verify. An invalid form must remain open with an error. Follow PHONE_ACCEPTANCE.md for drafts/rotation/keyboard/floating/browser checks.

Fresh 1.7.0 Actions and phone acceptance are pending. Baseline1.6.0 was validated/imported by Actions#29; local source/resource compilation is not certification. Optional workflow dispatch device_tests=true runs the API35 instrumentation suite on a disposable emulator; NEVER run the full Store test suite against personal rules.

Replacing only the ZIP cannot upgrade the app: the old importer pins its old package and ignores archive changes after installation. The matching workflow at the existing path is necessary. If owner/source edits are detected, do not delete the marker or force overwrite; return the log so a new baseline can be packaged.

The mobile archive includes all runtime art/source, the original room and new context sprites and guarded installer. Historical unused mini/source sprite-sheet duplicates are omitted and left untouched in the expanded repository. Feed-data deletion is not included.
