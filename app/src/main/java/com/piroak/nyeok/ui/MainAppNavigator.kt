package com.piroak.nyeok.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.dynamicfeatures.createGraph
import com.piroak.nyeok.ui.demo.DemoScreen
import com.piroak.nyeok.ui.demo.SearchScreen
import kotlinx.serialization.Serializable

@Composable
fun MainAppNavigator() {
    val navController = rememberNavController()
    val navGraph = remember(navController) {
        navController.createGraph(startDestination = Routes.Demo) {
            composable<Routes.Demo> { DemoScreen(onClick = { navController.navigate(Routes.Search) }) }
            composable<Routes.Search> { SearchScreen() }
        }
    }
    NavHost(navController, navGraph)
}

object Routes {
    @Serializable
    object Demo

    @Serializable
    object Search
}
