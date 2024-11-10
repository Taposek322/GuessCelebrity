package com.taposek322.guesscelebrity.data

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import com.taposek322.guesscelebrity.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.util.concurrent.ExecutionException
import java.util.regex.Matcher
import java.util.regex.Pattern
import javax.inject.Inject

private const val tag = "DownloadImageImpl"

class DownloadImageImpl @Inject constructor(
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : DownloadImage {
    override suspend fun getContent(address: String): Pair<List<String>, List<String>>? = withContext(ioDispatcher) {
        return@withContext try {
            val urls: MutableList<String> = mutableListOf()
            val names: MutableList<String> = mutableListOf()
            val content: String = downloadContent(address)!!
            Log.d(tag, "content = $content")
            val start = "<table class=\"common_rating_list"
            val finish = "</table>"
            val pattern: Pattern = Pattern.compile("$start(.*?)$finish")
            val matcher: Matcher = pattern.matcher(content)
            var splitContent: String? = ""
            while (matcher.find()) splitContent = matcher.group(1)
            Log.d(tag, "splitContent = $splitContent")
            val paternImg: Pattern = Pattern.compile("<img\\s+[^>]*src=\"([^\"]+)\"")
            val patternName: Pattern = Pattern.compile("<a class=\"profile_title\" href=\"[^\"]+\">(.*?)</a>")
            val matcherImg: Matcher = paternImg.matcher(splitContent)
            val matcherName: Matcher = patternName.matcher(splitContent)
            while (matcherImg.find()) urls.add(matcherImg.group(1))
            while (matcherName.find()) names.add(matcherName.group(1).trim())
            Log.d(tag, "urls = $urls")
            Log.d(tag, "names = $names")
            Pair(urls.toList(), names.toList())
        } catch (e: ExecutionException) {
            e.printStackTrace()
            null
        } catch (e: InterruptedException) {
            e.printStackTrace()
            null
        }
    }
    private suspend fun downloadContent(address: String) = withContext(ioDispatcher){
        var url: URL? = null
        var urlConnection: HttpURLConnection? = null
        val result = StringBuilder()

        return@withContext try {
            url = URL(address)
            urlConnection = url.openConnection() as HttpURLConnection
            val inputStream = urlConnection.inputStream
            val inputStreamReader = InputStreamReader(inputStream)
            val reader = BufferedReader(inputStreamReader)
            var line = reader.readLine()
            while (line != null) {
                result.append(line)
                line = reader.readLine()
            }
            result.toString()
        } catch (e: MalformedURLException) {
            e.printStackTrace()
            null
        } catch (e: IOException) {
            e.printStackTrace()
            null
        } finally {
            urlConnection?.disconnect()
        }
    }

    private suspend fun downloadImage(address: String) = withContext(ioDispatcher) {
        var url: URL? = null
        var urlConnection: HttpURLConnection? = null
        //val result = java.lang.StringBuilder()
        return@withContext try {
            url = URL(address)
            urlConnection = url.openConnection() as HttpURLConnection
            val inputStream = urlConnection.inputStream
            val bitmap = BitmapFactory.decodeStream(inputStream)
            bitmap
        } catch (e: MalformedURLException) {
            e.printStackTrace()
            null
        } catch (e: IOException) {
            e.printStackTrace()
            null
        } finally {
            urlConnection?.disconnect()
        }
    }
}