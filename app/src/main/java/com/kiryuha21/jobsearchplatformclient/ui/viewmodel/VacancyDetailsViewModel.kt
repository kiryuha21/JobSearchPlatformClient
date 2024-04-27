package com.kiryuha21.jobsearchplatformclient.ui.viewmodel

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.kiryuha21.jobsearchplatformclient.data.domain.Vacancy
import com.kiryuha21.jobsearchplatformclient.data.mappers.toDomainVacancy
import com.kiryuha21.jobsearchplatformclient.data.mappers.toVacancyDTO
import com.kiryuha21.jobsearchplatformclient.data.remote.AuthToken
import com.kiryuha21.jobsearchplatformclient.data.remote.RetrofitObject.vacancyRetrofit
import com.kiryuha21.jobsearchplatformclient.ui.contract.VacancyDetailsContract
import com.kiryuha21.jobsearchplatformclient.util.networkCallWithReturnWrapper
import com.kiryuha21.jobsearchplatformclient.util.networkCallWrapper
import com.kiryuha21.jobsearchplatformclient.util.toRequestBody
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody

class VacancyDetailsViewModel(
    private val navigateToEdit: () -> Unit,
    private val navigateWithPopCallback: () -> Unit,
    private val navigateCallback: () -> Unit,
) : BaseViewModel<VacancyDetailsContract.Intent, VacancyDetailsContract.State>() {
    override fun initialState(): VacancyDetailsContract.State {
        return VacancyDetailsContract.State(
            isLoading = false,
            openedVacancy = null
        )
    }

    override fun processIntent(intent: ViewIntent) {
        when (intent) {
            is VacancyDetailsContract.Intent.EditVacancy -> editVacancy(intent.vacancy, intent.bitmap)
            is VacancyDetailsContract.Intent.CreateVacancy -> createVacancy(intent.vacancy, intent.bitmap)
            is VacancyDetailsContract.Intent.DeleteVacancy -> deleteVacancy(intent.vacancyId)
            is VacancyDetailsContract.Intent.LoadVacancy -> loadVacancy(intent.vacancyId)
            is VacancyDetailsContract.Intent.OpenEdit -> {
                setState { copy(openedVacancy = intent.vacancy) }
                navigateToEdit()
            }
        }
    }

    private fun editVacancy(vacancy: Vacancy, bitmap: Bitmap?) {
        val token = viewModelScope.async(Dispatchers.IO) {
            networkCallWithReturnWrapper(
                networkCall = { "Bearer ${AuthToken.getToken()}" }
            )
        }

        val job = if (bitmap != null) {
            viewModelScope.launch(Dispatchers.IO) {
                val body = MultipartBody.Part.createFormData(
                    name = "picture",
                    filename = vacancy.id,
                    body = bitmap.toRequestBody(100)
                )
                networkCallWrapper(
                    networkCall = { vacancyRetrofit.setPicture(token.await().toString(), vacancy.id, body) }
                )
            }
        } else null

        viewModelScope.launch {
            setState { copy(isLoading = true) }

            val editedVacancy = withContext(Dispatchers.IO) {
                networkCallWithReturnWrapper(
                    networkCall = { vacancyRetrofit.editVacancy(token.await().toString(), vacancy.id, vacancy.toVacancyDTO()) }
                )
            }
            job?.join()

            if (editedVacancy != null) {
                setState { copy(openedVacancy = editedVacancy.toDomainVacancy(), isLoading = false) }
            } else {
                setState { copy(isLoading = false) }
            }
            navigateCallback()
        }
    }

    private fun createVacancy(vacancy: Vacancy, bitmap: Bitmap?) {
        val token = viewModelScope.async(Dispatchers.IO) {
            networkCallWithReturnWrapper(
                networkCall = { "Bearer ${AuthToken.getToken()}" }
            )
        }

        viewModelScope.launch {
            setState { copy(isLoading = true) }

            val newVacancy = withContext(Dispatchers.IO) {
                networkCallWithReturnWrapper(
                    networkCall = {
                        vacancyRetrofit.createNewVacancy(
                            token.await().toString(),
                            vacancy.toVacancyDTO()
                        )
                    }
                )
            }

            if (bitmap != null && newVacancy != null) {
                val body = MultipartBody.Part.createFormData(
                    name = "picture",
                    filename = vacancy.id,
                    body = bitmap.toRequestBody(100)
                )
                networkCallWrapper(
                    networkCall = { vacancyRetrofit.setPicture(token.await().toString(), newVacancy.id, body) }
                )
            }

            setState { copy(isLoading = false) }
            navigateWithPopCallback()
        }
    }

    private fun deleteVacancy(vacancyId: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                networkCallWrapper(
                    networkCall = { vacancyRetrofit.deleteVacancy("Bearer ${AuthToken.getToken()}", vacancyId) }
                )
            }
            navigateWithPopCallback()
        }
    }

    private fun loadVacancy(vacancyId: String) {
        viewModelScope.launch {
            setState { copy(isLoading = true) }
            val vacancy = withContext(Dispatchers.IO) {
                networkCallWithReturnWrapper(
                    networkCall = { vacancyRetrofit.getVacancyById(vacancyId).toDomainVacancy() }
                )
            }
            setState { copy(isLoading = false, openedVacancy = vacancy) }
        }
    }

    companion object {
        fun provideFactory(
            navigateToEdit: () -> Unit,
            navigateCallback: () -> Unit,
            navigateWithPopCallback: () -> Unit
        ) =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(
                    modelClass: Class<T>,
                    extras: CreationExtras
                ): T {
                    return VacancyDetailsViewModel(
                        navigateToEdit = navigateToEdit,
                        navigateCallback = navigateCallback,
                        navigateWithPopCallback = navigateWithPopCallback
                    ) as T
                }
            }
    }
}