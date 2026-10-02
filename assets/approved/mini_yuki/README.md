# Mini Yuki companion artwork

Owner supplied Mini-Yuki-Creation.zip and selected it as the companion identity. Sky-blue hoodie, white hair, bright blue eyes, navy leggings and fluffy white socks are preserved exactly. The original 8x11 atlas (192x208 cells) and source manifest are retained here. Runtime PNGs in drawable-nodpi are 57 final animation frames smoothly resampled to 384x416 using ImageMagick Mitchell filtering. This is a 2x upscale, not newly generated detail. All have real transparent alpha. Runtime rendering uses bitmap filtering as well.

Individual frames are upscaled separately to prevent atlas-cell bleeding. Frame counts/order/poses are preserved. The sixteen look directions remain in the ORIGINAL source atlas for future use; they are not part of the runtime companion state machine yet. The larger 2x atlas is intentionally omitted from the APK/source ZIP to avoid duplicating the runtime art and keep the mobile package below 20MB.

Idle uses authored blink frames, movement uses left/right gaits, Boop uses waving, warning uses waiting and restriction return uses failed. Carry uses the airborne jumping frame with a gentle suspension transform. It is not a custom hoodie-grab illustration. Existing large blocking-surface character art remains approved and unchanged. Feed/eating assets and the larger room are future work.
