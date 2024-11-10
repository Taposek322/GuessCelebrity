package com.taposek322.guesscelebrity.data

interface DownloadImage {
    suspend fun getContent(address: String): Pair<List<String>, List<String>>?
}