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

## After the 1.1 phone test
- Validate the modern shell and transparent intervention on the owner's phone, including fonts, landscape and keyboard.
- Specify optional AI dialogue separately: minimal disclosed context, local fallback, no model-controlled rule changes, secure API credential handling, and verified supported integration route. No direct relay is promised or implemented.

## Companion follow-ups
- 1.5.0 build/test/lint/import verified by Actions #27. Owner clarified that dashboard/navigation UX should be replaced.
- 1.6.0 candidate implements the explicitly accepted one-room flow: quiet room + adult Yuki, greeting, four choices, embedded bottom save-and-close panels. Require fresh Actions and phone/device acceptance before expanding.
- Review actual room scale, portrait/landscape, large text, TalkBack, keyboard and rotation on the owner's phone; refine expressions/room reactions after this flow is accepted.
- Historical UI boards are not adopted by this slice. Further artwork/themes are separate choices.
- Adult eating/feed-data design with explicit file selection and Android deletion consent remains unimplemented. No universal trash access assumed.
- Optional AI integration stays separately specified; local scripted dialogue is not AI conversation/ChatGPT relay. Open AI chat continues the existing external destination.
