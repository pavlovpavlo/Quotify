package com.kovhan.feature.splash.presentation.splash

/**
 * Playback facts of the intro Lottie, shared by the screen and its view model so
 * the hold-on-screen window cannot drift away from the animation's real length.
 *
 * `load_anim.json` runs frames 0..210 at 60 fps and its last keyframe sits at
 * frame 204, so the visible motion is over just before the composition ends.
 */
internal object SplashAnimation {

    const val SPEED = 1.25f

    private const val FRAMES = 210f
    private const val FRAME_RATE = 60f

    /** Wall-clock length of one pass at [SPEED]. */
    val MIN_VISIBLE_MS: Long = (FRAMES / FRAME_RATE / SPEED * 1000f).toLong()
}
