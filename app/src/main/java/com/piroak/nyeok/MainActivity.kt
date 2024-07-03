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
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.piroak.nyeok.ui.theme.NyeokTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NyeokTheme { // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
                ) {
                    EchoTest()
                }
            }
        }
    }
}


@Composable
fun EchoTest(modifier: Modifier = Modifier) {
    Column {
        Row {
            TextField(value = "xx", onValueChange = { /*TODO*/ })
            Button(onClick = { /*TODO*/ }) {}
        }
        Text("Server response: ")
    }
}

@Preview(showBackground = true)
@Composable
fun EchoTestPreview() {
    NyeokTheme {
        EchoTest()
    }
}
