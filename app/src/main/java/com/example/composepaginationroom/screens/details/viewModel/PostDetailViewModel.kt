package com.example.composepaginationroom.screens.details.viewModel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.composepaginationroom.screens.common.Constants
import com.example.composepaginationroom.screens.common.model.Post
import com.example.composepaginationroom.util.CPRDB
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val cprdb: CPRDB
) : ViewModel() {

    private val postId: String? = savedStateHandle[Constants.NavArgKeys.POST_ID]

    private val _postStateFlow = MutableStateFlow<Post?>(null)
    val postStateFlow = _postStateFlow.asStateFlow()

    init {
        postId?.let {
            viewModelScope.launch(Dispatchers.IO) {
                val post = cprdb.postDao().getPostById(postId.toInt())
                _postStateFlow.value = post
            }
        }
    }

}