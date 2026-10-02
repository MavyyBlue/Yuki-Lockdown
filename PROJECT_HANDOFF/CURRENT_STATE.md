# Current state — adult companion 1.4.0 candidate

Inspected baseline: main HEAD2e31eca0168e10b1754f8b3b57911d0015be673b, imported by successful Actions run21 /36964473011. That run built/tested/linted and compiled the device-test APK; optional emulator execution was skipped. New version1.4 has not run in GitHub yet.

## Scope

The user clarified that Boop must blow a kiss, and the IN-APP size setting must control the FLOATING companion. Home enlargement is outside this slice. Newly generated adult companion sheets provide idle/blink/walk/arms-crossed reaction, six kiss phases and six suspended hoodie-carry phases. Leftward walking mirrors the rightward frames. Kiss is one-shot, followed by idle/active reaction after1440ms; carry loops while Move is armed/dragging. No reduced-motion transforms are forced: static kiss/carry poses are used instead.

Floating height is80–240dp, default160dp, stored as pocket_yuki/size_dp. The view uses a2:3 width/height ratio with a48dp minimum touch width where the screen permits it. Viewport constraints prevent oversized windows. Existing normalized placement is restored when size/orientation changes. Dragged placement remains saved independently from nearby roaming. Existing hide/restore behavior is unchanged.

Rules, Store, UsageLedger, Device, BrowserDomains, GuardService, AndroidManifest and development signing key are byte-identical to the baseline. Original six approved intervention images/icon/wordmark are preserved. VersionCode5, versionName1.4.0. New adult artwork is a requested redesign candidate, not presumed approved. Original layout boards still await explicit approval. Feed-data deletion/eating and conversational Home remain future work.

## Validation

Independent API35 resource compile/link and main Java compilation passed. Existing25 policy tests plus4 meaningful size boundary tests passed;29 installer tests passed. Transparent generated sheets are1254×1254 originals, split deterministically into418×627 frames without upscaling/redrawing. Exact baseline upgrade, canonical payload checksum, archive CRC, workflow preservation, idempotence and policy/storage/signing byte checks pass. Full Gradle/lint and on-phone sprite timing/touch/service validation remain outstanding. Browser interaction acceptance remains outstanding.

## Delivery

yuki-lockdown-source-v1.4.0.zip and matching bootstrap.yml are separate. The ZIP includes buildable source/current artwork; obsolete unused mini runtime sprites/source atlas are omitted to keep mobile upload small. Existing obsolete repository files are retained harmlessly; no deletion is performed. tools/mature_baseline.json pins the inspected source. Importer validates all destinations before writing, skips workflow extraction and never force-pushes. On rerun, expanded source remains authoritative. See MOBILE_UPLOAD.md.
