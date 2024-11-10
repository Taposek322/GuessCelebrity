package com.taposek322.guesscelebrity.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.taposek322.guesscelebrity.domain.ApplicationInternalInteractor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GameScreenViewModel @Inject constructor(
    interactor: ApplicationInternalInteractor
) : ViewModel() {
    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    lateinit var gameImages: Pair<List<String>, List<String>>

    private val _imageIndex = MutableStateFlow(0)
    val imageIndex = _imageIndex.asStateFlow()

    private val _answerResult: MutableStateFlow<Boolean?> = MutableStateFlow(null)
    val answerResult = _answerResult.asStateFlow()

    init {
        viewModelScope.launch {
            _isLoading.value = true
            gameImages = interactor.getContent("https://web.archive.org/web/20210813084707/https://www.forbes.ru/rating/434657-50-samyh-uspeshnyh-zvezd-rossii-reyting-forbes")!!
            chooseImages()
            _isLoading.value = false
        }
    }

    fun chooseImages() {
        _imageIndex.value = (0..<gameImages.first.size).random()
    }

    fun checkAnswer(answer: String) {
        _answerResult.value = answer.trim().equals(gameImages.second[imageIndex.value], ignoreCase = true)
    }

    fun clearAnswerResult() {
        _answerResult.value = null
    }
}