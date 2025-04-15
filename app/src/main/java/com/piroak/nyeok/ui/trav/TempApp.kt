package com.piroak.nyeok.ui.trav

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.piroak.nyeok.ui.demo.fromHex

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApp()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyApp() {
    Scaffold(
        topBar = { TopAppBar(title = { Text(text = "Temp Top Bar") }, navigationIcon = { Icon(painter = painterResource(id = com.piroak.nyeok.R.drawable.ic_launcher_foreground), contentDescription = null) } ) }
    ) {
        paddingValues -> 
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Background gradient using Canvas
            Box(contentAlignment = Alignment.Center) {
                Canvas(
                    modifier = Modifier
                        .fillMaxSize(),
                    onDraw = {
                        // Apply radial gradient
                        val brush = Brush.radialGradient(
                            colors = listOf(Color.fromHex("#FE4C40"), Color.Transparent),
                            center = center,
                            radius = size.minDimension / 1.2F
                        )
                        drawRect(brush)
                    }
                )
                
                // Circular Map View
                Surface(
                    shape = CircleShape,
                    shadowElevation = 20.dp,
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .aspectRatio(1.0f)
                ) {
                    // Map Image (Placeholder)
                    Image(
                        painter = painterResource(id = com.piroak.nyeok.R.drawable.ic_launcher_background), // Replace with actual map image resource
                        contentDescription = "Map",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
    
            // Foreground content on top of the gradient background
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(space =  36.dp, alignment = Alignment.CenterHorizontally),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(40.dp)
                ) {
                    Image(
                        painter = painterResource(id = com.piroak.nyeok.R.drawable.current_location_10_128),
                        contentDescription = null,
                        modifier = Modifier.size(88.dp)
                    )

                    Text(
                        text = buildAnnotatedString {
                            withStyle(style = SpanStyle(fontSize = 48.sp, fontWeight = FontWeight.Bold, color = Color.Red)) {
                                append("38")
                            }

                            withStyle(style = SpanStyle(fontSize = 36.sp, fontWeight = FontWeight.SemiBold)) {
                                append("m 앞")
                            }
                        },
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MyApp()
}
