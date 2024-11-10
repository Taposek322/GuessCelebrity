package com.taposek322.guesscelebrity.domain

import com.taposek322.guesscelebrity.data.DownloadImage
import javax.inject.Inject

class ApplicationInternalInteractorImpl @Inject constructor(
    private val downloadImage: DownloadImage
) : ApplicationInternalInteractor{
    override suspend fun getContent(address: String): Pair<List<String>, List<String>>? = downloadImage.getContent(address)
}