# Current state — companion idle/wander fix1.4.1

Baseline main HEADe3704878258d8bbb28b198a716de7670e1c0b2e2. Latest green Actions run23 /36966842610 built/tested/linted1.4.0 and compiled its Android test APK; job110712333271 logs confirmed imported/pushed e370487. Emulator execution skipped. Owner supplied a10.2s phone recording: adult companion and menu appear, but repeated idle body drift looks like shuffling in place. Owner likes the redesigned companion. Broader real-device and browser acceptance remain incomplete.

## Fix
Idle previously swapped base00/01/05 every2.54s; their body centers/poses differ. IdleYukiDrawable now reuses base00 for ALL body pixels and draws softly masked, aligned eyelids from base01 for120ms every4.32s. Feet/body are fixed rather than simulating steps while stationary. No artwork changed or regenerated. Static reduced-motion idle stays base00. Kiss and carry sheets/sequences unchanged.

CompanionWalk replaces tiny24dp roaming with targets96dp either side of saved anchor. From the default/right edge it travels inward; at the left edge it picks the alternative reachable target. Actual position animation and gait start together only for nonzero paths. Trips run approximately32dp/second, clamped1.2–7s, then rest12s after arrival. The menu/carry/reactions/reduced motion pause roaming; drag placement remains independent. Existing lockout hide/restore is unchanged.

VersionCode6/versionName1.4.1. Size80–240dp preference unchanged. Rules, Store, UsageLedger, Device, BrowserDomains, GuardService, AndroidManifest and signing key are byte-identical to baseline. Companion artwork bytes, original intervention art/icon/wordmark preserved. Layout approval, feed-data deletion and conversational Home remain separate future work.

## Checks and delivery
Independent API35 resources/link/main Java compile passes.34 Java unit tests (including edge-route/speed/no-room checks) and33 importer tests pass. Added Android raster regression validates identical lower-body pixels during blink; INCLUDED but not executed here. SVG preview of the same eyelid compositing visually inspected. Full build/lint/instrumentation/phone verification require the new workflow run. Source ZIP simulation checks exact baseline upgrade, checksums/CRC, guarded owner edits, idempotence, workflow preservation and policy/storage/signing bytes.

Deliver1.4.1 ZIP plus matching bootstrap.yml. tools/idle_baseline.json pins inspected HEAD. Importer skips bundled workflow; owner replaces existing workflow manually. Obsolete unused mini sprites/source atlas omitted from ZIP for mobile size and left untouched in existing repository. Current runtime source/art are included. Follow MOBILE_UPLOAD.md.
