package rpt.tool.badpixelsearch.utils.managers

import android.app.Activity
import rpt.tool.badpixelsearch.utils.AppUtils

object ReviewPromptManager {
    
    private const val MIN_TESTS_BEFORE_PROMPT = 2
    private const val COOLDOWN_MS = 5L * 24 * 60 * 60 * 1000

    suspend fun onTestCompleted(activity: Activity) {
        val count = SharedPreferencesManager.completedTests + 1
        val lastPrompt = SharedPreferencesManager.lastPromptMs
        val now = System.currentTimeMillis()

        SharedPreferencesManager.completedTests = count

        val hasEnoughUsage = count >= MIN_TESTS_BEFORE_PROMPT
        val isCooldownOver = lastPrompt == 0L || (now - lastPrompt) >= COOLDOWN_MS

        if (hasEnoughUsage && isCooldownOver) {
            SharedPreferencesManager.lastPromptMs = now
            AppUtils.requestInAppReview(activity)
        }
    }
}
