package com.piroak.nyeok.ui

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.dynamicfeatures.createGraph
import com.piroak.nyeok.ui.demo.DemoScreen
import com.piroak.nyeok.ui.demo.DemoViewModel
import com.piroak.nyeok.ui.demo.SearchScreen
import com.piroak.nyeok.appViewModel
import kotlinx.serialization.Serializable

@Composable
fun MainAppNavigator() {
    val navController = rememberNavController()
    val navGraph = remember(navController) {
        navController.createGraph(startDestination = Routes.Demo) {
            composable<Routes.Demo> {
                DemoScreen(onSearchClick = { navController.navigate(Routes.Search) })
            }
            composable<Routes.Search> {
                val compassViewModel: DemoViewModel = appViewModel()
                SearchScreen(onResultClick = { document ->
                    Log.d("GUN", "DocumentItem clicked: ${document.place_name}")
                    compassViewModel.setDestination(document.place_name)
                    navController.popBackStack()
                })
            }
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
