package com.example.data.util

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

data class AutoSiteMetadata(
    val title: String,
    val desc: String,
    val iconUrl: String,
    val isSuccess: Boolean
)

object SiteMetadataFetcher {

    /**
     * Fast network metadata extractor.
     * Safely fetches <title>, meta description and favicon icon from the target URL.
     */
    suspend fun fetch(rawUrl: String): AutoSiteMetadata = withContext(Dispatchers.IO) {
        val trimmed = rawUrl.trim()
        if (trimmed.length < 4) {
            return@withContext AutoSiteMetadata("", "", "", false)
        }

        val formattedUrl = when {
            trimmed.startsWith("http://") || trimmed.startsWith("https://") -> trimmed
            else -> "https://$trimmed"
        }

        try {
            val urlObj = URL(formattedUrl)
            val domain = urlObj.host
            val conn = (urlObj.openConnection() as HttpURLConnection).apply {
                connectTimeout = 3500
                readTimeout = 3500
                instanceFollowRedirects = true
                setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36")
                setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
            }

            val responseCode = conn.responseCode
            if (responseCode in 200..399) {
                val reader = BufferedReader(InputStreamReader(conn.inputStream, "UTF-8"))
                val sb = StringBuilder()
                var line: String?
                var charCount = 0
                // Read up to 80KB (plenty for head tag)
                while (reader.readLine().also { line = it } != null && charCount < 80000) {
                    sb.append(line).append("\n")
                    charCount += (line?.length ?: 0)
                    if (line?.contains("</head>", ignoreCase = true) == true) break
                }
                reader.close()
                val html = sb.toString()

                // 1. Title Extraction
                val titleRegex = Regex("<title[^>]*>(.*?)</title>", RegexOption.IGNORE_CASE)
                val rawTitle = titleRegex.find(html)?.groupValues?.get(1)?.trim() ?: ""
                val cleanTitle = rawTitle
                    .replace("&amp;", "&")
                    .replace("&lt;", "<")
                    .replace("&gt;", ">")
                    .replace("&quot;", "\"")
                    .substringBefore(" - ")
                    .substringBefore(" | ")
                    .substringBefore(" _ ")
                    .substringBefore("【")
                    .trim()

                // 2. Description Extraction
                val descRegex = Regex("<meta[^>]*name=[\"']description[\"'][^>]*content=[\"'](.*?)[\"']", RegexOption.IGNORE_CASE)
                val rawDesc = descRegex.find(html)?.groupValues?.get(1)?.trim() ?: ""

                // 3. Icon Extraction
                val iconRegex = Regex("<link[^>]*rel=[\"'](?:shortcut )?icon[\"'][^>]*href=[\"'](.*?)[\"']", RegexOption.IGNORE_CASE)
                val iconHref = iconRegex.find(html)?.groupValues?.get(1)?.trim() ?: ""
                val resolvedIcon = when {
                    iconHref.startsWith("http://") || iconHref.startsWith("https://") -> iconHref
                    iconHref.startsWith("//") -> "https:$iconHref"
                    iconHref.startsWith("/") -> "https://$domain$iconHref"
                    iconHref.isNotBlank() -> "https://$domain/$iconHref"
                    else -> "https://$domain/favicon.ico"
                }

                AutoSiteMetadata(
                    title = if (cleanTitle.isNotBlank()) cleanTitle else domain,
                    desc = rawDesc,
                    iconUrl = resolvedIcon,
                    isSuccess = true
                )
            } else {
                val domain = urlObj.host
                AutoSiteMetadata(
                    title = domain,
                    desc = "",
                    iconUrl = "https://$domain/favicon.ico",
                    isSuccess = true
                )
            }
        } catch (e: Exception) {
            val domain = formattedUrl
                .removePrefix("https://")
                .removePrefix("http://")
                .removePrefix("www.")
                .substringBefore("/")
            AutoSiteMetadata(
                title = domain,
                desc = "",
                iconUrl = if (domain.isNotBlank()) "https://$domain/favicon.ico" else "",
                isSuccess = false
            )
        }
    }
}
