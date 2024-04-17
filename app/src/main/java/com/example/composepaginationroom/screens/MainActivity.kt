package com.example.composepaginationroom.screens

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.composepaginationroom.screens.common.Constants
import com.example.composepaginationroom.screens.details.ui.PostDetailScreen
import com.example.composepaginationroom.screens.posts.ui.PostScreen
import com.example.composepaginationroom.screens.posts.ui.PostsScreenEvent
import com.example.composepaginationroom.theme.ComposePaginationRoomTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposePaginationRoomTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    NavHost(navController, startDestination = Constants.Screens.POSTS.name) {
                        composable(Constants.Screens.POSTS.name) {
                            PostScreen {
                                when (it) {
                                    is PostsScreenEvent.PostClick -> {
                                        navController.navigate(
                                            "${Constants.Screens.POST_DETAILS.name}/{${Constants.NavArgKeys.POST_ID}}"
                                                .replace(
                                                    oldValue = "{${Constants.NavArgKeys.POST_ID}}",
                                                    newValue = "${it.post.id}"
                                                )
                                        )
                                    }
                                }
                            }
                        }
                        composable("${Constants.Screens.POST_DETAILS.name}/{${Constants.NavArgKeys.POST_ID}}") {
                            PostDetailScreen()
                        }
                    }
                }
            }
        }
    }

}