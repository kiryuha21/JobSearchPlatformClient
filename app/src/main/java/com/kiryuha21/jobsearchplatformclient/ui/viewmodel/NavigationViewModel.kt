package com.kiryuha21.jobsearchplatformclient.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.kiryuha21.jobsearchplatformclient.data.local.datastore.TokenDataStore
import com.kiryuha21.jobsearchplatformclient.ui.navigation.navigationDrawerItems
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Stack

class NavigationViewModel(
    private val tokenDatasourceProvider : TokenDataStore,
    private val logoutCallback : () -> Unit
) : ViewModel() {
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

    private fun logOut() {
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