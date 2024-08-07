package com.piroak.nyeok.ui.demo

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.piroak.nyeok.appViewModel
import com.piroak.nyeok.network.Document

@Composable
fun SearchScreen(
    searchViewModel: SearchViewModel = appViewModel(), onResultClick: (Document) -> Unit = {}
) {
    val userInput: String by searchViewModel.userInputFlow.collectAsState()
    val searchResults: List<Document> by searchViewModel.searchResults.collectAsState()

    Column {
        TextField(
            value = userInput,
            onValueChange = searchViewModel::onUserInputChanged,
            modifier = Modifier.fillMaxWidth()
        )

        LazyColumn {
            items(searchResults) { document ->
                DocumentItem(document = document,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onResultClick(document) }
                        .border(width = 1.dp, color = Color.Black)
                        .padding(16.dp))
            }
        }
    }
}

@Composable
fun DocumentItem(document: Document, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Text(text = document.place_name)
        Text(text = document.address_name)
    }
}

@Preview
@Composable
private fun SearchScreenPreview() {
    Surface(modifier = Modifier.fillMaxSize()) {
        SearchScreen()
    }
}
