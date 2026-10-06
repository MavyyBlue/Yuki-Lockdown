# Current state — 6 October 2026

Owner-directed 1.8.0/code10 integrates authenticated Local Yuki control. Existing signing certificate/keystore, rule safety, companion art and stored configuration remain intact. SQLite migration1→2 adds durable bridge receipts. Current implementation is described in README and the accompanying Local Yuki owner architecture document.

Baseline main d9874a44bba8a32c95c3a2cf5e63854fe86af33d and successful bootstrap run36979273834 were inspected before edits. Final source identity and exact-source CI are recorded separately in delivery evidence, never inferred from that baseline.

Automated local verification: 55 Java/Robolectric tests; assembly, lint and Android-test APK compilation. Authentication tests validate UID/package/certificate decisions and protocol tests exercise actual typed mutation dispatch; signed two-app Binder acceptance remains a real-device check. Guarded importer regressions and actual final-ZIP baseline overlay are checked separately. No other assistant certification is required.
