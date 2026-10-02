# Yuki Lockdown — adult companion 1.4.0 candidate

Boop now blows a kiss. Move uses dedicated dangling hoodie-carry frames. Settings → Floating Yuki size adjusts the floating overlay from 80–240dp height (default160dp), saves the choice, and fits the available display. The new adult companion keeps white hair, blue eyes and the cozy blue hoodie. These newly requested designs are review candidates; they do not alter the original six approved intervention images or imply layout approval.

Blinking idle, nearby wandering, warning/lockout reactions and saved normalized position remain. Reduced motion freezes frames and roaming; Boop shows a still kiss pose. The companion hides during interventions, keyboard use, screen lock and while this app is open. Home size/layout is unchanged by this setting. Feed-data deletion and the larger conversational room remain future work.

Upload **yuki-lockdown-source-v1.4.0.zip** at repository root and replace **.github/workflows/bootstrap.yml** with the separately supplied matching **bootstrap.yml**. Wait for green build/test/lint after BOTH uploads. Install its APK over the current app without clearing data. See PROJECT_HANDOFF/MOBILE_UPLOAD.md. The importer skips the workflow entry and guards existing source against accidental overwrites.

Inspected baseline HEAD2e31eca0168e10b1754f8b3b57911d0015be673b was imported by successful Actions run21 (36964473011). This candidate passes independent API35 resource/link/Java compilation, 29 Java unit tests and29 installer tests. Fresh full Gradle build/lint and real-phone acceptance remain pending.

Rules, persistence, usage/browser handling, application ID, permissions and signing key are preserved. The accessibility service must be connected. Talk opens configured ChatGPT; no chat API integration. Domain blocking uses supported visible browser address bars, not a firewall.
