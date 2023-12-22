package com.kiryuha21.jobsearchplatformclient.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector

@Composable
fun ErrorComponent(image: ImageVector, text: String, modifier: Modifier = Modifier) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Image(imageVector = image, contentDescription = text)
        Text(text = text)
    }
}

@Composable
fun FixableErrorComponent(
    errorImage: ImageVector,
    text: String,
    fixImage: ImageVector,
    fixFunction: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Image(imageVector = errorImage, contentDescription = text)
        Text(text = text)
        Button(onClick = fixFunction) {
            Image(imageVector = fixImage, contentDescription = "fix")
        }
    }
}