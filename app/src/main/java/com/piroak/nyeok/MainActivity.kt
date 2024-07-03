package com.piroak.nyeok

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
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
import com.piroak.nyeok.ui.theme.NyeokTheme
import com.piroak.nyeok.ui.view.SampleViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NyeokTheme { // A surface container using the 'background' color from the theme
                val viewModel = SampleViewModel()
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
                ) {
                    val uiState = viewModel.uiState.collectAsState()
                    EchoTest(
                        onSend = { value: String -> viewModel.sendEcho(value) },
                        responseMessage = uiState.value
                    )
                }
            }
        }
    }
}

@Composable
fun EchoTest(onSend: (String) -> Unit, responseMessage: String, modifier: Modifier = Modifier) {
    var inputMessage by remember {
        mutableStateOf("")
    }

    Column {
        Row {
            TextField(value = inputMessage, onValueChange = { value -> inputMessage = value })
            Button(onClick = {
                onSend(inputMessage)
            }) { Text("Send") }
        }
        Text("Server response: $responseMessage")
    }
}

@Preview(showBackground = true)
@Composable
fun EchoTestPreview() {
    NyeokTheme {
        EchoTest({ _ -> }, "Example Response")
    }
}
