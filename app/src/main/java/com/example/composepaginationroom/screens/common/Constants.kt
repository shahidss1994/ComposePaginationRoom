package com.example.composepaginationroom.screens.common

object Constants {

    object SharedPreference {

        const val SHARED_PREFERENCE_NAME = "cpr_pref"

        object SharedPreferenceKey {
            const val SCROLL_POSITION = "scroll_position"
        }
    }

    enum class Screens {
        POSTS,
        POST_DETAILS
    }

    object NavArgKeys {
        const val POST_ID = "POST_ID"
    }

}