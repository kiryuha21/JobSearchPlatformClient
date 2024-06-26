package com.kiryuha21.jobsearchplatformclient.ui.viewmodel

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.kiryuha21.jobsearchplatformclient.data.domain.Resume
import com.kiryuha21.jobsearchplatformclient.data.mappers.toDomainResume
import com.kiryuha21.jobsearchplatformclient.data.mappers.toResumeDTO
import com.kiryuha21.jobsearchplatformclient.di.AuthToken
import com.kiryuha21.jobsearchplatformclient.di.RetrofitObject.resumeRetrofit
import com.kiryuha21.jobsearchplatformclient.ui.contract.ResumeDetailsContract
import com.kiryuha21.jobsearchplatformclient.util.networkCallWithReturnWrapper
import com.kiryuha21.jobsearchplatformclient.util.networkCallWrapper
import com.kiryuha21.jobsearchplatformclient.util.toRequestBody
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody

class ResumeDetailsViewModel(
    private val navigateToProfileWithPop: () -> Unit,
    private val navigateToProfile: () -> Unit,
    private val navigateToEdit: () -> Unit,
) : BaseViewModel<ResumeDetailsContract.Intent, ResumeDetailsContract.State>() {
    override fun initialState(): ResumeDetailsContract.State {
        return ResumeDetailsContract.State(
            isLoadingResume = false,
            isSavingResume = false,
            openedResume = null,
            loadingText = ""
        )
    }

    override fun processIntent(intent: ViewIntent) {
        when (intent) {
            is ResumeDetailsContract.Intent.EditResume -> editResume(intent.resume, intent.bitmap)
            is ResumeDetailsContract.Intent.CreateResume -> createResume(intent.resume, intent.bitmap)
            is ResumeDetailsContract.Intent.LoadResume -> loadResume(intent.resumeId)
            is ResumeDetailsContract.Intent.OpenEdit -> navigateToEdit()
            is ResumeDetailsContract.Intent.DeleteResume -> deleteResume()
        }
    }

    private fun deleteResume() {
        val resumeId = viewState.openedResume?.id ?: throw Exception("resume can't be null here")

        viewModelScope.launch {
            setState { copy(isLoadingResume = true, loadingText = "Удаление резюме...") }

            withContext(Dispatchers.IO) {
                networkCallWrapper(
                    networkCall = { resumeRetrofit.deleteResume("Bearer ${AuthToken.getToken()}", resumeId) }
                )
            }

            setState { copy(isLoadingResume = false) }
            navigateToProfileWithPop()
        }
    }

    private fun editResume(resume: Resume, bitmap: Bitmap?) {
        val token = viewModelScope.async(Dispatchers.IO) {
            networkCallWithReturnWrapper(
                networkCall = { "Bearer ${AuthToken.getToken()}" }
            )
        }

        val job = if (bitmap != null) {
            viewModelScope.launch(Dispatchers.IO) {
                val body = MultipartBody.Part.createFormData(
                    name = "picture",
                    filename = resume.id,
                    body = bitmap.toRequestBody(100)
                )
                networkCallWrapper(
                    networkCall = { resumeRetrofit.setPicture(token.await().toString(), resume.id, body) }
                )
            }
        } else null

        viewModelScope.launch {
            setState { copy(isSavingResume = true, loadingText = "Сохранение изменений...") }

            val editedResume = withContext(Dispatchers.IO) {
                networkCallWithReturnWrapper(
                    networkCall = { resumeRetrofit.editResume(token.await().toString(), resume.id, resume.toResumeDTO()) }
                )
            }
            job?.join()

            if (editedResume != null) {
                setState { copy(openedResume = editedResume.toDomainResume(), isSavingResume = false) }
            } else {
                setState { copy(isSavingResume = false) }
            }
            navigateToProfile()
        }
    }

    private fun createResume(resume: Resume, bitmap: Bitmap?) {
        viewModelScope.launch {
            setState { copy(isSavingResume = true, loadingText = "Создание резюме...") }

            val resumeResult = withContext(Dispatchers.IO) {
                networkCallWithReturnWrapper(
                    networkCall = {
                        resumeRetrofit.createNewResume("Bearer ${AuthToken.getToken()}", resume.toResumeDTO())
                    }
                )
            }

            if (bitmap != null && resumeResult != null) {
                val body = MultipartBody.Part.createFormData(
                    name = "picture",
                    filename = resume.id,
                    body = bitmap.toRequestBody(100)
                )
                withContext(Dispatchers.IO) {
                    networkCallWrapper(
                        networkCall = {
                            resumeRetrofit.setPicture("Bearer ${AuthToken.getToken()}", resumeResult.id, body)
                        }
                    )
                }
            }

            setState { copy(isSavingResume = false) }
            navigateToProfileWithPop()
        }
    }

    private fun loadResume(resumeId: String) {
        viewModelScope.launch {
            setState { copy(isLoadingResume = true, loadingText = "Загрузка резюме...") }
            val resumeResult = withContext(Dispatchers.IO) {
                networkCallWithReturnWrapper(
                    networkCall = { resumeRetrofit.getPrivateResumeById(
                        authToken = "Bearer ${AuthToken.getToken()}",
                        resumeId = resumeId
                    ).toDomainResume() }
                )
            }
            setState { copy(isLoadingResume = false, openedResume = resumeResult) }
        }
    }

    companion object {
        fun provideFactory(
            navigateToProfile: () -> Unit,
            navigateToProfileWithPop: () -> Unit,
            navigateToEdit: () -> Unit
        ) =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(
                    modelClass: Class<T>,
                    extras: CreationExtras
                ): T {
                    return ResumeDetailsViewModel(
                        navigateToProfile = navigateToProfile,
                        navigateToProfileWithPop = navigateToProfileWithPop,
                        navigateToEdit = navigateToEdit
                    ) as T
                }
            }
    }
}