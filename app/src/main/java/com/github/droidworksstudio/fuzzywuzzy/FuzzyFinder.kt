package com.github.droidworksstudio.fuzzywuzzy

import com.github.droidworksstudio.launcher.data.entities.AppInfo
import java.text.Normalizer
import java.util.*
import kotlin.math.max // Import for maxOf if not already there

object FuzzyFinder {
    fun scoreApp(app: AppInfo, searchChars: String, topScore: Int): Int {
        if (searchChars.isEmpty()) {
            // Or return a default score, e.g., if you want all apps to show initially
            return 0 // No search term, no score or a specific default
        }
        if (app.appName.isEmpty()) {
            return 0 // No app name to search against
        }

        val normalizedAppName = normalizeStringInternal(app.appName)
        val normalizedSearchChars = normalizeStringInternal(searchChars)

        // If after normalization, either is empty, handle appropriately
        if (normalizedSearchChars.isEmpty()) return 0 // Or some other logic
        if (normalizedAppName.isEmpty()) return 0


        // Option 1: Your current subsequence logic (with ensured normalization)
        var fuzzyScore = calculateSubsequenceScore(normalizedAppName, normalizedSearchChars)

        // Option 2: Using Levenshtein distance for better fuzziness
        //var fuzzyScore = calculateLevenshteinSimilarity(normalizedAppName, normalizedSearchChars)
        if (normalizedAppName.first() == normalizedSearchChars.first()) {
            fuzzyScore += 0.5f
        }

        return (fuzzyScore * topScore).toInt()
    }

    // Renamed for clarity and internal use
    private fun normalizeStringInternal(input: String): String {
        // Remove diacritical marks and special characters, and convert to uppercase
        val normalized = Normalizer.normalize(input, Normalizer.Form.NFD)
        return normalized
            .replace(Regex("\\p{InCombiningDiacriticalMarks}+"), "")
            .replace(Regex("[-_+,.\\s]"), "") // Added \\s to remove spaces as well
            .uppercase(Locale.getDefault())
    }

    // Your original calculation, perhaps renamed
    private fun calculateSubsequenceScore(s1: String, s2: String): Float {
        val m = s1.length
        val n = s2.length

        if (n == 0) return 1.0f // Empty search query matches everything with full score
        if (m == 0) return 0.0f // Empty app name cannot match
        if (n > m) return 0.0f // Search string longer than app name (for subsequence)


        var matchCount = 0
        var s1Index = 0

        for (c2 in s2) {
            var found = false
            for (j in s1Index until m) {
                if (s1[j] == c2) {
                    found = true
                    s1Index = j + 1
                    break
                }
            }
            if (!found) {
                return 0f // Strict subsequence: if one char not found in order, score is 0
            }
            matchCount++
        }
        // Score based on how much of the search term was found, relative to search term length
        // Or, keep your original: matchCount.toFloat() / maxOf(m, n)
        // A common way for subsequence is matchCount / n
        return matchCount.toFloat() / n.toFloat()
    }

    fun normalizeString(appLabel: String, searchChars: String): Boolean {
        return (appLabel.contains(searchChars, true) or
                Normalizer.normalize(appLabel, Normalizer.Form.NFD)
                    .replace(Regex("\\p{InCombiningDiacriticalMarks}+"), "")
                    .replace(Regex("[-_+,. ]"), "")
                    .contains(searchChars, true))
    }

}
