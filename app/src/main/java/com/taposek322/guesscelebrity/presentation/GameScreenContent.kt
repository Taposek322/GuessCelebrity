package com.taposek322.guesscelebrity.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarData
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import kotlinx.coroutines.delay

@Composable
fun GameScreenContent(
    modifier: Modifier = Modifier,
    viewModel: GameScreenViewModel = hiltViewModel()
) {
    val isLoading by viewModel.isLoading.collectAsState()

    if (isLoading) {
        LoadingScreen(
            modifier = modifier
        )
    } else {
        val index by viewModel.imageIndex.collectAsState()
        val answerResult by viewModel.answerResult.collectAsState()
        if (answerResult == true) {
            viewModel.chooseImages()
        }
        GameScreen(
            imageUrl = viewModel.gameImages.first[index],
            onAnswerClick = viewModel::checkAnswer,
            answerResult = answerResult,
            onAnswerResultClear = viewModel::clearAnswerResult,
            modifier = modifier
        )
    }
}

@Composable
fun LoadingScreen(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
    ) {
        Icon(
            imageVector = Icons.Filled.Refresh,
            contentDescription = "Loading",
            modifier = Modifier
                .align(Alignment.Center)
        )
    }
}

@OptIn(ExperimentalCoilApi::class)
@Composable
fun GameScreen(
    imageUrl: String,
    onAnswerClick: (String) -> Unit,
    answerResult: Boolean?,
    onAnswerResultClear: () -> Unit,
    modifier: Modifier = Modifier
) {
    var answer by rememberSaveable {
        mutableStateOf("")
    }
    var snackbarText by rememberSaveable {
        mutableStateOf("")
    }
    val keyboardController = LocalSoftwareKeyboardController.current
    LaunchedEffect(answerResult) {
        answerResult?.let {
            snackbarText = if (it) "Correct" else "Incorrect"
            delay(2000)
            snackbarText = ""
            onAnswerResultClear()
        }
    }
    Box(
        modifier = modifier
            .fillMaxSize()
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier
                .fillMaxSize()
        ) {
            Image(
                painter = rememberImagePainter(imageUrl),
                contentDescription = "Image to guess",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .weight(1f)
                    .padding(48.dp)
                    .fillMaxSize()
            )
            TextField(
                value = answer,
                onValueChange = { answer = it },
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(
                    onDone = { onAnswerClick(answer) }
                )
            )
            Button(
                onClick = {
                    onAnswerClick(answer)
                    keyboardController?.hide()
                }
            ) {
                Text(
                    text = "Answer",
                    textAlign = TextAlign.Center
                )
            }
        }
        if (snackbarText.isNotEmpty()) {
            Snackbar(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(24.dp)
            ) {
                Text(
                    text = snackbarText,
                )
            }
        }
    }
}

