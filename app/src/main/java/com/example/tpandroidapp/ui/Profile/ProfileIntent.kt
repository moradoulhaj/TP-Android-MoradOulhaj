package com.example.tpandroidapp.ui.Profile


sealed class ProfileIntent {
    object LoadUser : ProfileIntent()
    object Logout : ProfileIntent()
}