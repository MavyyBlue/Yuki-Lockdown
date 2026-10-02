# Yuki Lockdown — companion idle/wander fix1.4.1

Fixes the owner's recording of repeated shuffling in place. Idle no longer cycles between differently positioned bodies: it keeps one exact body bitmap and composites only brief eyelid patches from existing approved companion art. Blink occurs once every4.32s, with120ms closed. No artwork regenerated; kiss, hoodie-carry and saved80–240dp sizing are preserved.

Wandering now travels up to96dp either side of saved placement, at roughly32dp/second (1.2–7s trips), followed by12s rest. It chooses an inward route at screen edges; zero-distance routes never play the gait. Movement still pauses while the menu is open, while carrying, during reactions, and with reduced motion. Hiding/lockout restores follow existing guard lifecycle. Roaming does not overwrite saved manual placement.

Inspected main HEADe3704878258d8bbb28b198a716de7670e1c0b2e2 was imported by successful Actions run23 /36966842610. This supersedes the previous pending1.4 CI note. The owner's video confirms the new character/overlay runs on their phone, but reports the idle drift fixed here; broader browser and device acceptance are still outstanding.

Independent API35 resource/link/Java compilation,34 Java tests and33 installer tests pass for this candidate. An Android rendering regression checks that every body pixel below the face stays identical across idle frames; it is included for CI/emulator execution and has NOT been executed here. Fresh full build/lint and phone acceptance remain pending.

Upload **yuki-lockdown-source-v1.4.1.zip** at repository root and replace **.github/workflows/bootstrap.yml** with the matching separate **bootstrap.yml**. Wait for green after both uploads, install over the existing app and keep its data. Rules/storage, original six intervention images, app ID, permissions and signing key are preserved.
