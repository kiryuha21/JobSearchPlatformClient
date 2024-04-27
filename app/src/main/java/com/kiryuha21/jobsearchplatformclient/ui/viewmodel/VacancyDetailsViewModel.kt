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
    private val navigateToProfileWithPop: () -> Unit,
    private val navigateToProfile: () -> Unit,
) : BaseViewModel<VacancyDetailsContract.Intent, VacancyDetailsContract.State>() {
    override fun initialState(): VacancyDetailsContract.State {
        return VacancyDetailsContract.State(
            isLoadingVacancy = false,
            isSavingVacancy = false,
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
            setState { copy(isSavingVacancy = true) }

            val editedVacancy = withContext(Dispatchers.IO) {
                networkCallWithReturnWrapper(
                    networkCall = { vacancyRetrofit.editVacancy(token.await().toString(), vacancy.id, vacancy.toVacancyDTO()) }
                )
            }
            job?.join()

            if (editedVacancy != null) {
                setState { copy(openedVacancy = editedVacancy.toDomainVacancy(), isSavingVacancy = false) }
            } else {
                setState { copy(isSavingVacancy = false) }
            }
            navigateToProfile()
        }
    }

    private fun createVacancy(vacancy: Vacancy, bitmap: Bitmap?) {
        val token = viewModelScope.async(Dispatchers.IO) {
            networkCallWithReturnWrapper(
                networkCall = { "Bearer ${AuthToken.getToken()}" }
            )
        }

        viewModelScope.launch {
            setState { copy(isSavingVacancy = true) }

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

            setState { copy(isSavingVacancy = false) }
            navigateToProfileWithPop()
        }
    }

    private fun deleteVacancy(vacancyId: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                networkCallWrapper(
                    networkCall = { vacancyRetrofit.deleteVacancy("Bearer ${AuthToken.getToken()}", vacancyId) }
                )
            }
            navigateToProfileWithPop()
        }
    }

    private fun loadVacancy(vacancyId: String) {
        viewModelScope.launch {
            setState { copy(isLoadingVacancy = true) }
            val vacancy = withContext(Dispatchers.IO) {
                networkCallWithReturnWrapper(
                    networkCall = { vacancyRetrofit.getVacancyById(vacancyId).toDomainVacancy() }
                )
            }
            setState { copy(isLoadingVacancy = false, openedVacancy = vacancy) }
        }
    }

    companion object {
        fun provideFactory(
            navigateToEdit: () -> Unit,
            navigateToProfile: () -> Unit,
            navigateToProfileWithPop: () -> Unit
        ) =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(
                    modelClass: Class<T>,
                    extras: CreationExtras
                ): T {
                    return VacancyDetailsViewModel(
                        navigateToEdit = navigateToEdit,
                        navigateToProfile = navigateToProfile,
                        navigateToProfileWithPop = navigateToProfileWithPop
                    ) as T
                }
            }
    }
}