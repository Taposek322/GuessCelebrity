package com.taposek322.guesscelebrity.domain

interface ApplicationInternalInteractor {
    suspend fun getContent(address: String): Pair<List<String>, List<String>>?
}