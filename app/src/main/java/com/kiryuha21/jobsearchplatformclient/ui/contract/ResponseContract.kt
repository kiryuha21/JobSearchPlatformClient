package com.kiryuha21.jobsearchplatformclient.ui.contract

import com.kiryuha21.jobsearchplatformclient.data.domain.Resume
import com.kiryuha21.jobsearchplatformclient.ui.viewmodel.ViewIntent
import com.kiryuha21.jobsearchplatformclient.ui.viewmodel.ViewState

enum class ResponseStages {
    ChooseResume,
    WriteMessage
}

const val RESUMES_LOADING_TEXT = "Загружаем резюме"
const val RESPONSE_SAVING_TEXT = "Сохраняем отклик"
const val RESPONSE_SENT_SUCCESS = "Отклик отправлен"
const val RESPONSE_SENT_ERROR = "Ошибка при отправке отклика"

sealed class ResponseContract {
    data class State(
        val isLoading: Boolean,
        val loadingText: String,
        val resumes: List<Resume>,
        val selectedResume: Resume,
        val message: String,
        val stage: ResponseStages
    ) : ViewState

    sealed interface Intent : ViewIntent {
        data class ChooseResume(val resume: Resume) : Intent
        data class SetResponseStage(val responseStage: ResponseStages) : Intent
        data class UpdateMessage(val newMessage: String) : Intent
        data object CreateResponse : Intent
    }
}