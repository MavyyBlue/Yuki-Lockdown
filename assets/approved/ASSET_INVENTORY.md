# Yuki Lockdown — Asset Inventory

## Visual Direction

Yuki Lockdown should feel clean, cozy, playful, and slightly possessive rather than harsh or clinical.

The app uses a dark charcoal / black interface with white text, icy-blue accents, soft glow effects, rounded cards, and minimal visual clutter. Chibi Yuki is the emotional centerpiece of the experience and appears as a transparent character overlay when a Lockdown rule activates.

Yuki should remain visually consistent across all states:

- Long, straight white hair with subtle silver-blue highlights
- Bright blue eyes
- Pale skin
- Mature adult chibi proportions
- Clean modern anime-chibi linework
- Soft cel shading
- Expressive but readable facial expressions
- Compact silhouette suitable for narrow mobile screens
- Transparent background for all character overlays

The app UI itself should be built natively wherever possible. Text, buttons, cards, timers, progress bars, schedules, and speech bubbles should not be baked into character artwork.

---

## MVP Character Assets

Recommended master export: **2048 × 2048 PNG**, transparent background.

### 1. `yuki_neutral.png`

Default Yuki state.

**Look:** Warm smile, relaxed posture, oversized black hoodie, calm and affectionate expression.

**Use:** Home screen, onboarding, unrestricted/default app state.

---

### 2. `yuki_warning.png`

Gentle first-warning state.

**Look:** Arms crossed or one hand on hip, mildly stern but affectionate expression.

**Use:** First attempts to reopen a restricted app or when approaching a usage limit.

---

### 3. `yuki_annoyed.png`

Escalated Lockdown reaction.

**Look:** Slight pout, narrowed eyes, visibly unimpressed expression, stronger arms-crossed pose.

**Use:** Repeated attempts to access a blocked app.

---

### 4. `yuki_bedtime.png`

Bedtime Lockdown state.

**Look:** Cozy oversized sleep shirt or hoodie, pajama bottoms or shorts, fluffy socks, slightly messy white hair, sleepy eyes, optional pillow or blanket.

**Mood:** Sleepy, affectionate, but firmly commanding.

**Use:** Scheduled nighttime Lockdowns such as 12:00 AM–8:00 AM.

Example dialogue:

> “Darling, it’s midnight. Bed. Now. ♡”

---

### 5. `yuki_focus.png`

Focus / productivity state.

**Look:** Hair tied back or neatly arranged, optional glasses, focused expression, practical cozy outfit.

**Mood:** Intelligent, attentive, mildly strict.

**Use:** Work, study, productivity, or custom Focus schedules.

---

### 6. `yuki_outdoors.png`

Touch Grass / outdoor state.

**Look:** Comfortable casual outdoor clothes, optional light jacket or oversized hoodie, energetic expression.

**Mood:** Playful and insistent.

**Use:** Outdoor breaks, extended social-media lockouts, or “Touch Grass” profiles.

Example dialogue:

> “Go touch grass, Darling!”

---

## Branding Assets

### 7. `yuki_lockdown_app_icon`

Android adaptive application icon.

**Concept:** Chibi Yuki peeking over, hugging, or guarding a lock or shield.

The design should remain readable when cropped into Android launcher shapes such as circles, rounded squares, and squircles.

Required exports should include:

- Adaptive foreground
- Adaptive background
- Monochrome icon
- High-resolution source

---

### 8. `yuki_lockdown_logo.svg`

Primary wordmark/logo for Yuki Lockdown.

**Look:** Clean modern lettering with subtle icy-blue accents and a small lock motif.

Avoid overly decorative typography so the branding remains readable at mobile sizes.

---

## Interface Icons

Prefer **SVG** for all interface icons.

### 9. `icon_lock.svg`

Simple lock or shield-lock symbol.

**Use:** Active Lockdown status, blocked apps, protected schedules.

### 10. `icon_moon.svg`

Minimal moon symbol.

**Use:** Bedtime schedules.

### 11. `icon_focus.svg`

Focus symbol such as a target, eye, or concentration mark.

**Use:** Work / study Lockdowns.

### 12. `icon_outdoors.svg`

Leaf, grass, or small nature symbol.

**Use:** Outdoor / Touch Grass profiles.

---

## Optional Future Reaction Assets

These are not required for the first working build.

Suggested transparent SVG or PNG effects:

- `reaction_anger.svg`
- `reaction_sweat.svg`
- `reaction_heart.svg`
- `reaction_sparkle.svg`
- `reaction_zzz.svg`
- `reaction_exclamation.svg`
- `reaction_lock.svg`
- `reaction_leaf.svg`
- `reaction_moon.svg`

These can layer over Yuki rather than requiring entirely new character illustrations.

---

## Optional Modular Face Assets

If the art pipeline supports modular character animation, separate facial elements may later include:

- `eyes_open`
- `eyes_blink`
- `eyes_annoyed`
- `eyes_sleepy`
- `mouth_smile`
- `mouth_talking`
- `mouth_pout`

This would allow simple blinking, talking, and reaction animation without maintaining dozens of full character renders.

---

## Future Animation Assets

Not required for MVP.

Potential later states:

- Idle breathing
- Blinking
- Talking mouth
- Annoyed bounce
- Entrance pop
- Sleepy sway
- Tapping foot
- Blanket movement
- Lock-closing animation
- Yuki walking onto screen

Preferred future formats can be evaluated later between Lottie, WebP sequences, or lightweight sprite animation.

---

## Asset Design Rules

1. Keep Yuki readable at small mobile sizes.
2. Keep her silhouette compact enough for screen overlays.
3. All Yuki overlay assets must use transparent backgrounds.
4. Do not bake dialogue into character images.
5. UI elements should remain native Android components whenever possible.
6. Maintain consistent facial structure, hair length, eye color, proportions, and rendering style across every Yuki state.
7. Keep important visual details away from extreme edges of adaptive icons.
8. Preserve high-resolution source artwork separately from optimized Android exports.

---

## Minimum Asset Package for First Build

The first production implementation only requires:

1. `yuki_neutral.png`
2. `yuki_warning.png`
3. `yuki_annoyed.png`
4. `yuki_bedtime.png`
5. `yuki_focus.png`
6. `yuki_outdoors.png`
7. Yuki Lockdown Android app icon
8. `yuki_lockdown_logo.svg`
9. `icon_lock.svg`
10. `icon_moon.svg`
11. `icon_focus.svg`
12. `icon_outdoors.svg`

Everything else can be added after the first real-phone Lockdown implementation is functioning reliably.
