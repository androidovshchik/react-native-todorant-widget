package com.todorant.widget

import android.text.SpannableString
import android.text.Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
import android.text.style.URLSpan

private val linkRegex = "https?://[^\\s]+".toRegex()

fun String.formatLinks(): CharSequence {
    var out = this
    var result = linkRegex.find(out)
    val links = mutableListOf<String>()
    val positions = mutableListOf<Int>()
    while (result != null) {
        positions.add(result.range.first)
        val link = out.substring(result.range)
        links.add(link)
        val start = link.indexOf("/") + 2
        val end = link.indexOf("/", start)
        val domain = if (end > 0) {
            if (end < link.length - 1) {
                "${link.substring(start, end + 1)}..."
            } else {
                link.substring(start, end)
            }
        } else {
            link.substring(start)
        }
        out = out.replaceRange(result.range, domain)
        val last = result.range.first + domain.length
        positions.add(last)
        result = linkRegex.find(out, last)
    }
    if (links.isEmpty()) {
        return out
    }
    return SpannableString(out).apply {
        for (i in positions.indices step 2) {
            setSpan(URLSpan(links[i / 2]), positions[i], positions[i + 1], SPAN_EXCLUSIVE_EXCLUSIVE)
        }
    }
}