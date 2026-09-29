# Roadmap

## Required before calling production-ready
- Green build/test/lint on the exact packaged source; instrumentation on API26 and API35.
- Complete real-phone checklist, including Samsung/Pixel/OEM service lifecycle and permission revocation.
- Verify supported browser URL bar IDs on installed versions; communicate partial web coverage.
- Review accessibility, large text, edge-to-edge insets and overlay behavior on target phones.
- Private release signing, reproducible release process and distribution/privacy review.

## Later improvements
- More browser adapters with targeted regression coverage.
- Evaluate optional local VPN/domain filtering only with an explicit design for encrypted DNS, VPN conflicts, owner exemptions and honest limitations; no network-wide enforcement is currently claimed.
- Config export/import with versioned validation; migrations; finer diagnostics without browsing logs.
- Additional approved character themes/custom profile styles and localization.

No cloud service, ads, analytics, remote controls or undefeatable locking are planned.
