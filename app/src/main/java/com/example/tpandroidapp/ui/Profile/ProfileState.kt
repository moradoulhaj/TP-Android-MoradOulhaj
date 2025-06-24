package com.example.tpandroidapp.ui.Profile

import com.example.tpandroidapp.data.model.Cart
import com.example.tpandroidapp.data.model.UserData


sealed class ProfileState {
    object Loading : ProfileState()
    data class Success(
  val user:UserData
    ) : ProfileState()
    data class Error(val message: String) : ProfileState()
    object LoggedOut : ProfileState()
}