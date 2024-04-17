package com.example.composepaginationroom.screens.details.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.composepaginationroom.screens.details.viewModel.PostDetailViewModel

@Composable
fun PostDetailScreen() {
    val postsViewModel = hiltViewModel<PostDetailViewModel>()
    val post = postsViewModel.postStateFlow.collectAsStateWithLifecycle()
    Column(modifier = Modifier.padding(vertical = 20.dp, horizontal = 8.dp)) {

        Text(
            text = post.value?.title ?: "",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(15.dp)
        )

        Text(
            text = post.value?.body ?: "",
            fontSize = 16.sp,
        )
    }
}