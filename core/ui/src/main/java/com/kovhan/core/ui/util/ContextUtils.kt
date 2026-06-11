package com.kovhan.core.ui.util

import android.content.Context
import android.content.res.Configuration
import android.util.DisplayMetrics
import android.util.Log
import kotlin.math.min
import kotlin.math.roundToInt

object ContextUtils {

    private const val TAG = "screen_size_tag"

    private const val PHONE_DESIGN_WIDTH_DP = 375f

    // 0.0 = жорстко 375dp (старе); 1.0 = повністю native; 0.5 = середнє
    private const val SCREEN_ADAPTATION = 0.5f

    private const val ZOOM_IN_DAMPING = 0.3f
    private const val MAX_SYSTEM_SCALE = 1.15f

    private const val TABLET_SMALLEST_WIDTH_DP = 600f

    private const val STABLE_DP_WIDTH_MIN = 300f
    private const val STABLE_DP_WIDTH_MAX = 450f
    private const val FHD_REFERENCE_WIDTH = 1080

    fun updateConfiguration(context: Context): Context {
        val resources = context.resources
        val displayMetrics = resources.displayMetrics

        val currentWidthDp = displayMetrics.widthPixels / displayMetrics.density
        val currentHeightDp = displayMetrics.heightPixels / displayMetrics.density
        val smallestScreenWidthDp = min(currentWidthDp, currentHeightDp)
        val isPhone = smallestScreenWidthDp < TABLET_SMALLEST_WIDTH_DP

        // --- Крок 1: Адаптація під розмір екрану ---
        // Визначаємо native ширину пристрою (dp при дефолтному зумі).
        val effectiveBaseDpi = computeEffectiveBaseDpi(displayMetrics, isPhone)
        val nativeWidthDp = displayMetrics.widthPixels / (effectiveBaseDpi / 160f)

        // Інтерполяція між дизайн-шириною (375dp) та native шириною.
        // Великий екран (411dp) → адаптована ширина > 375dp → елементи менші за дизайн.
        // Малий екран (360dp) → адаптована ширина < 375dp → елементи більші за дизайн.
        val adaptedWidthDp = PHONE_DESIGN_WIDTH_DP +
            (nativeWidthDp - PHONE_DESIGN_WIDTH_DP) * SCREEN_ADAPTATION
        val figmaScale = displayMetrics.widthPixels / adaptedWidthDp

        // --- Крок 2: Системний скейлінг ---
        val densityZoom = displayMetrics.densityDpi.toFloat() / effectiveBaseDpi.toFloat()
        val fontScale = resources.configuration.fontScale

        val rawSystemScale = if (fontScale != 1f) {
            if (fontScale <= 1f) densityZoom * fontScale
            else (densityZoom + fontScale) / 2f
        } else {
            densityZoom
        }

        val clampedSystemScale = if (rawSystemScale <= 1f) {
            rawSystemScale
        } else {
            val dampened = 1f + (rawSystemScale - 1f) * ZOOM_IN_DAMPING
            dampened.coerceAtMost(MAX_SYSTEM_SCALE)
        }

        val targetDensity = figmaScale * clampedSystemScale
        val targetDensityDpi = (targetDensity * 160f).roundToInt()

        val configuration = Configuration(resources.configuration)
        configuration.densityDpi = targetDensityDpi
        configuration.fontScale = 1f

        val effectiveWidthDp = displayMetrics.widthPixels / (targetDensityDpi / 160f)

        Log.d(TAG, "=== ContextUtils.updateConfiguration ===")
        Log.d(TAG, "INPUT: widthPx=${displayMetrics.widthPixels}, densityDpi=${displayMetrics.densityDpi}, STABLE=${DisplayMetrics.DENSITY_DEVICE_STABLE}, fontScale=$fontScale")
        Log.d(TAG, "SCREEN: nativeWidthDp=$nativeWidthDp → adaptedWidthDp=$adaptedWidthDp (design=$PHONE_DESIGN_WIDTH_DP, adaptation=$SCREEN_ADAPTATION)")
        Log.d(TAG, "SYSTEM: densityZoom=$densityZoom, rawScale=$rawSystemScale → finalScale=$clampedSystemScale")
        Log.d(TAG, "OUTPUT: densityDpi=$targetDensityDpi, fontScale=1.0, widthDp=$effectiveWidthDp")
        Log.d(TAG, "========================================")

        return context.createConfigurationContext(configuration)
    }

    /**
     * On Samsung devices with resolution switching (WQHD+/FHD+/HD+),
     * [DisplayMetrics.DENSITY_DEVICE_STABLE] is calibrated for FHD+ (1080px).
     * When the device runs at a different resolution, the stable dp width
     * falls outside the normal phone range (300–450dp).
     *
     * In that case we scale [DENSITY_DEVICE_STABLE] proportionally to the
     * resolution change from FHD+, keeping the zoom detection resolution-independent.
     */
    private fun computeEffectiveBaseDpi(
        displayMetrics: DisplayMetrics,
        isPhone: Boolean,
    ): Int {
        val stableDpWidth =
            displayMetrics.widthPixels / (DisplayMetrics.DENSITY_DEVICE_STABLE / 160f)

        val needsResolutionCorrection = isPhone &&
            (stableDpWidth < STABLE_DP_WIDTH_MIN || stableDpWidth > STABLE_DP_WIDTH_MAX)

        return if (needsResolutionCorrection) {
            (displayMetrics.widthPixels.toLong() * DisplayMetrics.DENSITY_DEVICE_STABLE / FHD_REFERENCE_WIDTH).toInt()
        } else {
            DisplayMetrics.DENSITY_DEVICE_STABLE
        }
    }
}
