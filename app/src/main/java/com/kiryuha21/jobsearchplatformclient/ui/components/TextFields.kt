package com.kiryuha21.jobsearchplatformclient.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.sp

@Composable
fun Title(text: String, modifier: Modifier = Modifier) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
    ) {
        Text(
            text = text,
            fontFamily = FontFamily.SansSerif,
            fontSize = 20.sp
        )
    }
}

@Composable
fun DefaultTextField(icon: ImageVector, placeholder: String, modifier: Modifier = Modifier) {
    var text by remember {
        mutableStateOf("")
    }

    OutlinedTextField(
        value = text,
        modifier = modifier,
        label = { Text(placeholder) },
        leadingIcon = { Icon(imageVector = icon, contentDescription = "icon") },
        onValueChange = { text = it },
        placeholder = { Text(text = placeholder) },
    )
}

@Composable
fun PasswordTextField(icon: ImageVector, placeholder: String, modifier: Modifier = Modifier) {
    var text by remember { mutableStateOf("") }
    var visible by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = text,
        modifier = modifier,
        label = { Text(placeholder) },
        onValueChange = { text = it },
        placeholder = { Text(text = placeholder) },
        leadingIcon = { Icon(imageVector = icon, contentDescription = "icon") },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        visualTransformation = if (visible) VisualTransformation.None else PasswordVisualTransformation(),
        trailingIcon = {
            val image = if (visible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
            val description = if (visible) "Скрыть пароль" else "Показать пароль"

            IconButton(onClick = { visible = !visible }) {
                Icon(
                    imageVector = image,
                    contentDescription = description
                )
            }
        }
    )
}