# Yuki Lockdown 1.8 — authenticated Local Yuki control

Existing protection rules, plans, guard, room/companion artwork and signing are preserved. This owner-directed update adds a typed version1 Binder ContentProvider bridge so Local Yuki can inspect and mutate app-owned settings without UI automation. Enable **Allow Local Yuki control** in Settings; it defaults off.

The provider authenticates Binder UID/package against Local Yuki's original pinned certificate before parsing. Local Yuki independently checks Lockdown's original certificate. Both existing signing keys remain unchanged. Version/request/command/state-digest integrity, optimistic concurrency and durable idempotent receipts reject forged callers, stale state, duplicate changes and unknown interrupted mutation.

Controls cover protected applications/domains/plans, limits/schedules/modes, companion enablement/wander/size/position, reduced motion, dialogue/reactions, attempt reset and temporary breaks. Existing minimum bypass countdown and safe-app invariants remain enforced. No banked-time feature exists in the live app. Database1→2 only adds the bridge receipt journal.

Local full Gradle unit/build/lint and Android-test APK compilation passed; compilation is not emulator/device execution. Baseline inspected: main `d9874a44bba8a32c95c3a2cf5e63854fe86af33d`, successful bootstrap Actions #31 / `36979273834`. The delivery report records new exact source/CI outcomes and package hashes. See [current state](PROJECT_HANDOFF/CURRENT_STATE.md) and [phone acceptance](PROJECT_HANDOFF/PHONE_ACCEPTANCE.md).

Mobile delivery is the complete `yuki-lockdown-source-v1.8.0.zip` plus the matching protected `bootstrap.yml` if the live workflow has not already been updated. Upload the intact ZIP at repository root and replace `.github/workflows/bootstrap.yml` with the supplied file. The guarded importer checks the complete payload, preserves workflows/signing and refuses unexpected owner edits before writing. Install the resulting established-signature APK over the existing app without uninstalling.

The mobile ZIP includes all application source, runtime artwork/resources, Gradle/tools/tests and documentation. Unused original image sheets under repository-root assets/ remain in the existing repository and are omitted from the overlay to fit GitHub mobile’s25MiB upload limit. Their provenance documents are included; no runtime resource or source code is omitted. Fresh application builds use the included app resources, not those archival sheets.
