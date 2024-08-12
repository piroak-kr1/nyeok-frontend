package com.piroak.nyeok.ui.demo

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.piroak.nyeok.appViewModel

@Composable
fun TransitScreen(
    viewModel: TransitViewModel = appViewModel()
) {
    Text(text = "Transit Screen")
    Button(onClick = viewModel::xx) {
        Text(text = "Get Transit Data")
    }
}