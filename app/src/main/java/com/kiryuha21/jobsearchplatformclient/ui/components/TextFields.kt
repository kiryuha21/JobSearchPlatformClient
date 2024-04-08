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
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
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
fun SecuredTextField(
    icon: ImageVector,
    enabled: Boolean,
    placeholder: String,
    initString: String,
    onUpdate: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var text by remember { mutableStateOf(initString) }

    OutlinedTextField(
        value = text,
        modifier = modifier,
        enabled = enabled,
        label = { Text(placeholder) },
        leadingIcon = { Icon(imageVector = icon, contentDescription = "icon") },
        placeholder = { Text(text = placeholder) },
        onValueChange = {
            text = it
            onUpdate(text)
        }
    )
}

@Composable
fun DefaultTextField(
    icon: ImageVector,
    placeholder: String,
    initString: String,
    onUpdate: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    SecuredTextField(
        icon = icon,
        enabled = true,
        placeholder = placeholder,
        initString = initString,
        onUpdate = onUpdate,
        modifier = modifier
    )
}

@Composable
fun NumericTextField(
    placeholder: String,
    initString: String,
    onUpdate: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var text by remember { mutableStateOf(initString) }

    OutlinedTextField(
        value = text,
        modifier = modifier,
        label = { Text(placeholder) },
        placeholder = { Text(text = placeholder) },
        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
        onValueChange = {
            text = it
            onUpdate(text)
        }
    )
}

@Composable
fun PhoneField(
    initString: String,
    placeholder: String,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    mask: String = "000 000 00 00",
    maskNumber: Char = '0',
    onUpdate: (String) -> Unit
) {
    var text by remember { mutableStateOf(initString) }

    OutlinedTextField(
        value = initString,
        onValueChange = { it ->
            text = it.take(mask.count { it == maskNumber })
            onUpdate(it.take(mask.count { it == maskNumber }))
        },
        leadingIcon = { Icon(imageVector = icon, contentDescription = "icon") },
        placeholder = { Text(text = placeholder) },
        label = { Text(text = placeholder) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
        visualTransformation = PhoneVisualTransformation(mask, maskNumber),
        modifier = modifier
    )
}

class PhoneVisualTransformation(private val mask: String, private val maskNumber: Char) : VisualTransformation {

    private val maxLength = mask.count { it == maskNumber }

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

@Composable
fun SecuredPasswordTextField(
    icon: ImageVector,
    enabled: Boolean,
    placeholder: String,
    initString: String,
    isError: Boolean,
    supportingText: String,
    onUpdate: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var text by remember { mutableStateOf(initString) }
    var visible by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = text,
        modifier = modifier,
        label = { Text(placeholder) },
        onValueChange = {
            text = it
            onUpdate(text)
        },
        enabled = enabled,
        isError = isError,
        supportingText = {
            if (supportingText.isNotEmpty()) {
                Text(text = supportingText)
            }
        },
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

@Composable
fun PasswordTextField(
    icon: ImageVector,
    placeholder: String,
    initString: String,
    isError: Boolean,
    supportingText: String,
    onUpdate: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    SecuredPasswordTextField(
        icon = icon,
        enabled = true,
        placeholder = placeholder,
        initString = initString,
        isError = isError,
        supportingText = supportingText,
        onUpdate = onUpdate,
        modifier = modifier
    )
}