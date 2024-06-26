package com.kiryuha21.jobsearchplatformclient.ui.components.primary

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.text.KeyboardActions
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
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.TextUnit

@Composable
fun Title(text: String, fontSize: TextUnit, modifier: Modifier = Modifier) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
    ) {
        Text(
            text = text,
            fontFamily = FontFamily.SansSerif,
            fontSize = fontSize
        )
    }
}

@Composable
fun TextBox(
    placeholder: String,
    initString: String,
    onUpdate: (String) -> Unit,
    minLines: Int,
    modifier: Modifier = Modifier,
) {
    val focusManager = LocalFocusManager.current
    var text by remember { mutableStateOf(initString) }

    OutlinedTextField(
        value = text,
        onValueChange = {
            text = it
            onUpdate(text)
        },
        keyboardActions = KeyboardActions(onNext = {
            focusManager.moveFocus(FocusDirection.Down)
        }),
        minLines = minLines,
        modifier = modifier,
        placeholder = { Text(text = placeholder) },
        label = { Text(placeholder) },
    )
}

@Composable
fun SecuredTextField(
    text: String,
    icon: ImageVector?,
    enabled: Boolean,
    placeholder: String,
    onUpdate: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String = placeholder
) {
    val focusManager = LocalFocusManager.current

    OutlinedTextField(
        value = text,
        modifier = modifier,
        enabled = enabled,
        label = { Text(label) },
        leadingIcon = if (icon != null) {
            { Icon(imageVector = icon, contentDescription = "icon") }
        } else null,
        placeholder = { Text(text = placeholder) },
        singleLine = true,
        keyboardActions = KeyboardActions(onNext = {
            focusManager.moveFocus(FocusDirection.Down)
        }),
        onValueChange = { onUpdate(it) }
    )
}

@Composable
fun DefaultTextField(
    text: String,
    icon: ImageVector?,
    placeholder: String,
    onUpdate: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    SecuredTextField(
        text = text,
        icon = icon,
        enabled = true,
        placeholder = placeholder,
        onUpdate = onUpdate,
        modifier = modifier
    )
}

@Composable
fun ValidatedTextField(
    text: String,
    placeholder: String,
    onUpdate: (String, Boolean) -> Unit,
    isValid: (String) -> Boolean,
    errorMessage: String,
    modifier: Modifier = Modifier,
    label: String = placeholder,
    icon: ImageVector? = null,
    maxLength: Int = Int.MAX_VALUE,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None
) {
    val focusManager = LocalFocusManager.current
    var supportingText by remember { mutableStateOf(if (isValid(text)) "" else errorMessage) }

    OutlinedTextField(
        value = text,
        modifier = modifier,
        label = { Text(label) },
        placeholder = { Text(text = placeholder) },
        keyboardOptions = keyboardOptions,
        keyboardActions = KeyboardActions(onNext = {
            focusManager.moveFocus(FocusDirection.Down)
        }),
        singleLine = true,
        isError = supportingText.isNotEmpty(),
        supportingText = { Text(text = supportingText, color = Color.Red) },
        leadingIcon = if (icon != null) {
            { Icon(imageVector = icon, contentDescription = "icon") }
        } else null,
        visualTransformation = visualTransformation,
        onValueChange = {
            if (it.length <= maxLength) {
                supportingText = if (isValid(it)) "" else errorMessage
                onUpdate(it, supportingText.isEmpty())
            }
        }
    )
}

@Composable
fun SecuredPasswordTextField(
    text: String,
    icon: ImageVector,
    enabled: Boolean,
    placeholder: String,
    isError: Boolean,
    supportingText: String,
    onUpdate: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String = placeholder
) {
    var visible by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = text,
        modifier = modifier,
        label = { Text(label) },
        onValueChange = { onUpdate(it) },
        enabled = enabled,
        isError = isError,
        supportingText = {
            if (isError) {
                Text(text = supportingText)
            }
        },
        placeholder = { Text(text = placeholder) },
        leadingIcon = { Icon(imageVector = icon, contentDescription = "icon") },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        visualTransformation = if (visible) VisualTransformation.None else PasswordVisualTransformation(),
        singleLine = true,
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

@Composable
fun PasswordTextField(
    text: String,
    icon: ImageVector,
    placeholder: String,
    isError: Boolean,
    supportingText: String,
    onUpdate: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    SecuredPasswordTextField(
        text = text,
        icon = icon,
        enabled = true,
        placeholder = placeholder,
        isError = isError,
        supportingText = supportingText,
        onUpdate = onUpdate,
        modifier = modifier
    )
}

@Composable
fun PhoneField(
    text: String,
    placeholder: String,
    isValid: (String) -> Boolean,
    errorMessage: String,
    onUpdate: (String, Boolean) -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    mask: String = "000 000 00 00",
    maskNumber: Char = '0'
) {
    val transformation = PhoneVisualTransformation(mask, maskNumber)

    ValidatedTextField(
        text = text,
        placeholder = placeholder,
        onUpdate = { newText, valid ->
            onUpdate(newText.take(mask.count { it == maskNumber }), valid)
        },
        icon = icon,
        isValid = isValid,
        errorMessage = errorMessage,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone, imeAction = ImeAction.Next),
        visualTransformation = transformation,
        modifier = modifier,
        maxLength = transformation.maxLength
    )
}

class PhoneVisualTransformation(private val mask: String, private val maskNumber: Char) : VisualTransformation {
    val maxLength = mask.count { it == maskNumber }

    override fun filter(text: AnnotatedString): TransformedText {
        val trimmed = if (text.length > maxLength) text.take(maxLength) else text

        val annotatedString = buildAnnotatedString {
            if (trimmed.isEmpty()) return@buildAnnotatedString

            var maskIndex = 0
            var textIndex = 0
            while (textIndex < trimmed.length && maskIndex < mask.length) {
                if (mask[maskIndex] != maskNumber) {
                    val nextDigitIndex = mask.indexOf(maskNumber, maskIndex)
                    append(mask.substring(maskIndex, nextDigitIndex))
                    maskIndex = nextDigitIndex
                }
                append(trimmed[textIndex++])
                maskIndex++
            }
        }

        return TransformedText(annotatedString, PhoneOffsetMapper(mask, maskNumber))
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is PhoneVisualTransformation) return false
        if (mask != other.mask) return false
        if (maskNumber != other.maskNumber) return false
        return true
    }

    override fun hashCode(): Int {
        return mask.hashCode()
    }
}

private class PhoneOffsetMapper(val mask: String, val numberChar: Char) : OffsetMapping {
    override fun originalToTransformed(offset: Int): Int {
        var noneDigitCount = 0
        var i = 0
        while (i < offset + noneDigitCount) {
            if (mask[i++] != numberChar) noneDigitCount++
        }
        return offset + noneDigitCount
    }

    override fun transformedToOriginal(offset: Int): Int =
        offset - mask.take(offset).count { it != numberChar }
}