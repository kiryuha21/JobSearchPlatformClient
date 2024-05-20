package com.kiryuha21.jobsearchplatformclient.ui.components.auth

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Abc
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Password
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.kiryuha21.jobsearchplatformclient.data.domain.MIN_PASSWORD_LENGTH
import com.kiryuha21.jobsearchplatformclient.ui.components.primary.DefaultTextField
import com.kiryuha21.jobsearchplatformclient.ui.components.primary.PasswordTextField
import com.kiryuha21.jobsearchplatformclient.ui.components.primary.SecuredButton
import com.kiryuha21.jobsearchplatformclient.ui.components.special.MultiToggleButton
import com.kiryuha21.jobsearchplatformclient.ui.components.special.ToggleButtonElement
import com.kiryuha21.jobsearchplatformclient.ui.components.special.roleToggleItems
import com.kiryuha21.jobsearchplatformclient.ui.contract.AuthContract

@Composable
fun SignUpForm(
    onRegister: () -> Unit,
    onLoginFieldUpdated: (String) -> Unit,
    onEmailFieldUpdated: (String) -> Unit,
    onPasswordFieldUpdated: (String) -> Unit,
    onPasswordRepeatFieldUpdated: (String) -> Unit,
    onRoleToggled: (ToggleButtonElement) -> Unit,
    state: AuthContract.AuthState,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        DefaultTextField(
            text = state.username,
            icon = Icons.Filled.Abc,
            placeholder = "Логин",
            onUpdate = onLoginFieldUpdated,
            modifier = Modifier.padding(start = 5.dp, end = 5.dp, top = 10.dp, bottom = 10.dp)
        )
        DefaultTextField(
            text = state.email,
            icon = Icons.Filled.Email,
            placeholder = "E-mail",
            onUpdate = onEmailFieldUpdated,
            modifier = Modifier.padding(start = 5.dp, end = 5.dp, top = 15.dp, bottom = 10.dp)
        )

        val passwordTooShort = state.password.length < MIN_PASSWORD_LENGTH
        PasswordTextField(
            text = state.password,
            icon = Icons.Filled.Password,
            placeholder = "Пароль",
            onUpdate = onPasswordFieldUpdated,
            isError = passwordTooShort,
            supportingText = if (passwordTooShort) "Пароль слишком короткий" else "",
            modifier = Modifier.padding(start = 5.dp, end = 5.dp, top = 15.dp, bottom = 5.dp).testTag("signup_password")
        )

        val passwordsNotMatch = state.password != state.passwordRepeat
        PasswordTextField(
            text = state.passwordRepeat,
            icon = Icons.Filled.Password,
            placeholder = "Повторите пароль",
            onUpdate = onPasswordRepeatFieldUpdated,
            isError = passwordsNotMatch,
            supportingText = if (passwordsNotMatch) "Пароли не совпадают" else "",
            modifier = Modifier.padding(5.dp).testTag("signup_password_repeat")
        )
        MultiToggleButton(
            toggleStates = roleToggleItems,
            onToggleChange = onRoleToggled,
            modifier = Modifier.fillMaxWidth(0.9F)
        )
        SecuredButton(
            text = "Зарегистрироваться",
            onClick = { onRegister() },
            enabled = !passwordTooShort && !passwordsNotMatch,
            modifier = Modifier
                .padding(top = 20.dp)
                .defaultMinSize(minWidth = 200.dp)
                .testTag("signup_button")
        )
    }
}