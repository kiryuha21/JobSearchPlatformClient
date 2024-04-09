package com.kiryuha21.jobsearchplatformclient.ui.viewmodel

import android.graphics.Bitmap
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.navigation.NavController
import com.kiryuha21.jobsearchplatformclient.data.domain.CurrentUser
import com.kiryuha21.jobsearchplatformclient.data.domain.Resume
import com.kiryuha21.jobsearchplatformclient.data.domain.Vacancy
import com.kiryuha21.jobsearchplatformclient.data.mappers.toDomainResume
import com.kiryuha21.jobsearchplatformclient.data.mappers.toDomainVacancy
import com.kiryuha21.jobsearchplatformclient.data.mappers.toResumeDTO
import com.kiryuha21.jobsearchplatformclient.data.mappers.toVacancyDTO
import com.kiryuha21.jobsearchplatformclient.data.remote.AuthToken
import com.kiryuha21.jobsearchplatformclient.data.remote.RetrofitObject
import com.kiryuha21.jobsearchplatformclient.data.remote.api.ResumeAPI
import com.kiryuha21.jobsearchplatformclient.data.remote.api.UserAPI
import com.kiryuha21.jobsearchplatformclient.data.remote.api.VacancyAPI
import com.kiryuha21.jobsearchplatformclient.ui.contract.MainAppContract
import com.kiryuha21.jobsearchplatformclient.ui.navigation.NavigationGraph
import com.kiryuha21.jobsearchplatformclient.ui.navigation.NavigationGraph.MainApp.PROFILE
import com.kiryuha21.jobsearchplatformclient.ui.navigation.NavigationGraph.MainApp.RESUME_DETAILS_BASE
import com.kiryuha21.jobsearchplatformclient.ui.navigation.NavigationGraph.MainApp.RESUME_EDIT
import com.kiryuha21.jobsearchplatformclient.ui.navigation.NavigationGraph.MainApp.VACANCY_DETAILS_BASE
import com.kiryuha21.jobsearchplatformclient.ui.navigation.NavigationGraph.MainApp.VACANCY_EDIT
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.ByteArrayOutputStream

class MainAppViewModel(private val navController: NavController) :
    BaseViewModel<MainAppContract.MainAppIntent, MainAppContract.MainAppState>() {
    private val resumeRetrofit = RetrofitObject.retrofit.create(ResumeAPI::class.java)
    private val vacancyRetrofit = RetrofitObject.retrofit.create(VacancyAPI::class.java)
    private val userRetrofit = RetrofitObject.retrofit.create(UserAPI::class.java)
    private val user by CurrentUser.info

    override fun initialState(): MainAppContract.MainAppState {
        return MainAppContract.MainAppState(
            isLoading = false,
            vacancies = null,
            resumes = null,
            openedVacancy = null,
            openedResume = null
        )
    }

    override fun processIntent(intent: ViewIntent) {
        when (intent) {
            is MainAppContract.MainAppIntent.OpenVacancyEdit -> {
                setState { copy(openedVacancy = intent.vacancy) }
                navController.navigate(VACANCY_EDIT)
            }
            is MainAppContract.MainAppIntent.OpenResumeEdit -> {
                setState { copy(openedResume = intent.resume) }
                navController.navigate(RESUME_EDIT)
            }
            is MainAppContract.MainAppIntent.FindMatchingVacancies -> findMatchingVacancies()
            is MainAppContract.MainAppIntent.FindMatchingResumes -> findMatchingResumes()
            is MainAppContract.MainAppIntent.LoadProfileVacancies -> loadProfileVacancies()
            is MainAppContract.MainAppIntent.LoadProfileResumes -> loadProfileResumes()
            is MainAppContract.MainAppIntent.LogOut -> logOut()
            is MainAppContract.MainAppIntent.OpenResumeDetails -> openResumeDetails(intent.resumeId)
            is MainAppContract.MainAppIntent.CreateNewResume -> createNewResume(intent.resume, intent.bitmap)
            is MainAppContract.MainAppIntent.EditResume -> editResume(intent.resume, intent.bitmap)
            is MainAppContract.MainAppIntent.DeleteResume -> deleteResume(intent.resume.id)
            is MainAppContract.MainAppIntent.OpenVacancyDetails -> openVacancyDetails(intent.vacancyId)
            is MainAppContract.MainAppIntent.CreateNewVacancy -> createNewVacancy(intent.vacancy, intent.bitmap)
            is MainAppContract.MainAppIntent.EditVacancy -> editVacancy(intent.vacancy, intent.bitmap)
            is MainAppContract.MainAppIntent.DeleteVacancy -> deleteVacancy(intent.vacancy.id)
            is MainAppContract.MainAppIntent.SetUserImage -> setUserImage(intent.bitmap)
        }
    }

    private fun setUserImage(bitmap: Bitmap) {
        viewModelScope.launch(Dispatchers.IO) {
            val token = "Bearer ${AuthToken.getToken()}"
            val stream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
            val byteArray = stream.toByteArray()

            val body = MultipartBody.Part.createFormData(
                "picture",
                user.username,
                byteArray.toRequestBody("image/jpeg".toMediaTypeOrNull(), 0, byteArray.size)
            )
            userRetrofit.setPicture(token, user.username, body)
        }
    }

    private fun findMatchingVacancies() {
        viewModelScope.launch(Dispatchers.IO) {
            setState { copy(isLoading = true) }
            val vacancies = vacancyRetrofit.getMatchingVacancies().map { it.toDomainVacancy() }
            setState { copy(isLoading = false, vacancies = vacancies) }
        }
    }

    private fun findMatchingResumes() {
        viewModelScope.launch(Dispatchers.IO) {
            setState { copy(isLoading = true) }
            val resumes = resumeRetrofit.getMatchingResumes().map { it.toDomainResume() }
            setState { copy(isLoading = false, resumes = resumes) }
        }
    }

    private fun loadProfileResumes() {
        viewModelScope.launch(Dispatchers.IO) {
            setState { copy(isLoading = true) }
            val resumes = resumeRetrofit.getResumesByWorkerLogin(user.username).map { it.toDomainResume() }
            setState { copy(isLoading = false, resumes = resumes) }
        }
    }

    private fun loadProfileVacancies() {
        viewModelScope.launch(Dispatchers.IO) {
            setState { copy(isLoading = true) }
            val vacancies = vacancyRetrofit.getVacanciesByEmployerLogin(user.username).map { it.toDomainVacancy() }
            setState { copy(isLoading = false, vacancies = vacancies) }
        }
    }

    private fun openResumeDetails(resumeId: String) {
        navController.navigate("$RESUME_DETAILS_BASE/$resumeId")
        viewModelScope.launch {
            setState { copy(isLoading = true) }
            val resume = resumeRetrofit.getResumeById(resumeId).toDomainResume()
            setState { copy(isLoading = false, openedResume = resume) }
        }
    }

    private fun createNewResume(resume: Resume, bitmap: Bitmap?) {
        viewModelScope.launch(Dispatchers.IO) {
            val token = "Bearer ${AuthToken.getToken()}"
            val newResume = resumeRetrofit.createNewResume(token, resume.toResumeDTO())

            if (bitmap != null) {
                val stream = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream)
                val byteArray = stream.toByteArray()
                val body = MultipartBody.Part.createFormData(
                    "picture",
                    resume.id,
                    byteArray.toRequestBody("image/jpeg".toMediaTypeOrNull(), 0, byteArray.size)
                )
                resumeRetrofit.setPicture(token, newResume.id, body)
            }
        }
        navController.popBackStack()
        navController.navigate(PROFILE)
    }

    private fun editResume(resume: Resume, bitmap: Bitmap?) {
        viewModelScope.launch(Dispatchers.IO) {
            val token = "Bearer ${AuthToken.getToken()}"
            val editedResume = resumeRetrofit.editResume(token, resume.id, resume.toResumeDTO())

            if (bitmap != null) {
                val stream = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream)
                val byteArray = stream.toByteArray()
                val body = MultipartBody.Part.createFormData(
                    "picture",
                    resume.id,
                    byteArray.toRequestBody("image/jpeg".toMediaTypeOrNull(), 0, byteArray.size)
                )
                resumeRetrofit.setPicture(token, editedResume.id, body)
            }
            setState { copy(openedResume = editedResume.toDomainResume()) }
        }
        navController.navigate(PROFILE)
    }

    private fun deleteResume(resumeId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            resumeRetrofit.deleteResume("Bearer ${AuthToken.getToken()}", resumeId)
        }
        navController.popBackStack()
        navController.navigate(PROFILE)
    }

    private fun openVacancyDetails(vacancyId: String) {
        navController.navigate("$VACANCY_DETAILS_BASE/$vacancyId")
        viewModelScope.launch {
            setState { copy(isLoading = true) }
            val vacancy = vacancyRetrofit.getVacancyById(vacancyId).toDomainVacancy()
            setState { copy(isLoading = false, openedVacancy = vacancy) }
        }
    }

    private fun createNewVacancy(vacancy: Vacancy, bitmap: Bitmap?) {
        viewModelScope.launch(Dispatchers.IO) {
            val token = "Bearer ${AuthToken.getToken()}"
            val newResume = vacancyRetrofit.createNewVacancy(token, vacancy.toVacancyDTO())

            if (bitmap != null) {
                val stream = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream)
                val byteArray = stream.toByteArray()
                val body = MultipartBody.Part.createFormData(
                    "picture",
                    vacancy.id,
                    byteArray.toRequestBody("image/jpeg".toMediaTypeOrNull(), 0, byteArray.size)
                )
                vacancyRetrofit.setPicture(token, newResume.id, body)
            }
        }
        navController.popBackStack()
        navController.navigate(PROFILE)
    }

    private fun editVacancy(vacancy: Vacancy, bitmap: Bitmap?) {
        viewModelScope.launch(Dispatchers.IO) {
            val token = "Bearer ${AuthToken.getToken()}"
            val editedVacancy = vacancyRetrofit.editVacancy(token, vacancy.id, vacancy.toVacancyDTO())

            if (bitmap != null) {
                val stream = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream)
                val byteArray = stream.toByteArray()
                val body = MultipartBody.Part.createFormData(
                    "picture",
                    vacancy.id,
                    byteArray.toRequestBody("image/jpeg".toMediaTypeOrNull(), 0, byteArray.size)
                )
                vacancyRetrofit.setPicture(token, editedVacancy.id, body)
            }
            setState { copy(openedVacancy = editedVacancy.toDomainVacancy()) }
        }
        navController.navigate(PROFILE)
    }

    private fun deleteVacancy(vacancyId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            vacancyRetrofit.deleteVacancy("Bearer ${AuthToken.getToken()}", vacancyId)
        }
        navController.popBackStack()
        navController.navigate(PROFILE)
    }

    private fun logOut() {
        navController.navigate(NavigationGraph.Authentication.LOG_IN) {
            popUpTo(NavigationGraph.MainApp.NAV_ROUTE) {
                inclusive = true
            }
        }
    }

    companion object {
        fun provideFactory(navController: NavController) =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(
                    modelClass: Class<T>,
                    extras: CreationExtras
                ): T {
                    return MainAppViewModel(navController) as T
                }
            }
    }
}