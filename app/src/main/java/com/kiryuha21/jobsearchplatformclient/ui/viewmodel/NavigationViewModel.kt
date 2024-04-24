package com.kiryuha21.jobsearchplatformclient.ui.viewmodel

import android.graphics.Bitmap
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.kiryuha21.jobsearchplatformclient.data.domain.CurrentUser
import com.kiryuha21.jobsearchplatformclient.data.local.datastore.TokenDataStore
import com.kiryuha21.jobsearchplatformclient.data.remote.AuthToken
import com.kiryuha21.jobsearchplatformclient.data.remote.RetrofitObject
import com.kiryuha21.jobsearchplatformclient.data.remote.api.UserAPI
import com.kiryuha21.jobsearchplatformclient.ui.navigation.navigationDrawerItems
import com.kiryuha21.jobsearchplatformclient.util.networkCallWithReturnWrapper
import com.kiryuha21.jobsearchplatformclient.util.networkCallWrapper
import com.kiryuha21.jobsearchplatformclient.util.toRequestBody
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody
import java.util.Stack

class NavigationViewModel(
    private val tokenDatasourceProvider : TokenDataStore,
    private val logoutCallback : () -> Unit
) : ViewModel() {
    private val userRetrofit by lazy { RetrofitObject.retrofit.create(UserAPI::class.java) }
    private val navigationIndexStack = Stack<Int>()
    private val _currentIndex = mutableIntStateOf(0)
    val currentIndex by _currentIndex
    private var lastIndex = 0

    fun navigateTo(destination: String) {
        navigationDrawerItems.forEachIndexed { index, item ->
            if (item.navigationRoute == destination) {
                _currentIndex.intValue = index
                lastIndex = index
                return@forEachIndexed
            }
        }

        navigationIndexStack.push(lastIndex)
    }

    fun navigateBack() {
        if (navigationIndexStack.isNotEmpty()) {
            navigationIndexStack.pop()
        }

        _currentIndex.intValue = if (navigationIndexStack.empty()) 0 else navigationIndexStack.peek()
    }

    fun setUserImage(bitmap: Bitmap) {
        val token = viewModelScope.async(Dispatchers.IO) {
            networkCallWithReturnWrapper(networkCall = {
                "Bearer ${AuthToken.getToken()}"
            })
        }

        viewModelScope.launch(Dispatchers.IO) {
            val body = MultipartBody.Part.createFormData(
                name = "picture",
                filename = CurrentUser.info.username,
                body = bitmap.toRequestBody(100)
            )

            networkCallWrapper(
                networkCall = { userRetrofit.setPicture(token.await().toString(), CurrentUser.info.username, body) }
            )
        }
    }

    fun logOut() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                tokenDatasourceProvider.deleteRefreshToken()
            }
            logoutCallback()
        }
    }

    companion object {
        fun provideFactory(tokenDatasourceProvider : TokenDataStore, logoutCallback: () -> Unit) =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(
                    modelClass: Class<T>,
                    extras: CreationExtras
                ): T {
                    return NavigationViewModel(tokenDatasourceProvider, logoutCallback) as T
                }
            }
    }
}