package com.piroak.nyeok.ui.demoselect

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.AsyncImage
import com.piroak.nyeok.common.Coordinate
import com.piroak.nyeok.common.Place
import com.piroak.nyeok.common.PlaceAndDistance

@Composable
fun DemoSelectScreen(modifier: Modifier = Modifier) {
}

@Composable
fun PlaceAndDistanceDisplay(placeAndDistance: PlaceAndDistance, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Text(
            text = placeAndDistance.place.title,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        AsyncImage(
            model = placeAndDistance.place.firstimage2,
            contentDescription = null,
            modifier = Modifier
                .aspectRatio(1f, matchHeightConstraintsFirst = false)
                .clip(CircleShape)
                .align(Alignment.CenterHorizontally)
        )
    }
}

@Preview
@Composable
private fun PlaceOptionPreview() {
    PlaceAndDistanceDisplay(
        PlaceAndDistance(
        place = Place(
            title = "가미우동",
            contentid = 2679033,
            coordinate = Coordinate(37.5547407496, 126.9238735556),
            firstimage2 = "http://tong.visitkorea.or.kr/cms/resource/64/2676864_image2_1.jpg",
        ),
            distance_meter = 1020.57861F
        )
    )
}