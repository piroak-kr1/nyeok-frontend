package com.piroak.nyeok.ui.demo

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.piroak.nyeok.AppViewModelProvider

@Composable
fun SearchScreen(searchViewModel: SearchViewModel = viewModel(factory = AppViewModelProvider.Factory)) {
    val userInput: String by searchViewModel.userInputFlow.collectAsState()
    val searchResults: List<String> by searchViewModel.searchResults.collectAsState()

    Column {
        TextField(
            value = userInput,
            onValueChange = searchViewModel::onUserInputChanged,
            modifier = Modifier.fillMaxWidth()
        )

        searchResults.forEach { result ->
            Text(text = result)
        }
    }
}

@Preview
@Composable
private fun SearchScreenPreview() {
    Surface(modifier = Modifier.fillMaxSize()) {
        SearchScreen()
    }
}
