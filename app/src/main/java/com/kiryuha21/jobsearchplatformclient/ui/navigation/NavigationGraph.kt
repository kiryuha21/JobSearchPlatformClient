package com.kiryuha21.jobsearchplatformclient.ui.navigation

object NavigationGraph {
    object Authentication {
        const val NAV_ROUTE = "Authentication"
        const val LOG_IN = "Log In"
        const val SIGN_UP = "Sign Up"
        const val RESET_PASSWORD = "Reset Password"
    }

    object MainApp {
        const val NAV_ROUTE = "MainApp"
        const val HOME_SCREEN = "Home Screen"
        const val PROFILE = "Profile"
        const val SETTINGS = "Settings"
        const val VACANCY_DETAILS_BASE = "Vacancy"
        const val VACANCY_DETAILS = "$VACANCY_DETAILS_BASE/{vacancyId}"
        const val RESUME_DETAILS_BASE = "Resume"
        const val RESUME_DETAILS = "$RESUME_DETAILS_BASE/{resumeId}"
    }
}