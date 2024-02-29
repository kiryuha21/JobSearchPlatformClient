package com.kiryuha21.jobsearchplatformclient.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kiryuha21.jobsearchplatformclient.ui.theme.interFontFamily

@Composable
fun DefaultButton(text: String, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Button(
        onClick = onClick,
        modifier = modifier
    ) {
        Text(text = text)
    }
}

@Composable
fun FramelessButton(text: String, onClick: () -> Unit, modifier: Modifier = Modifier) {
    TextButton(
        onClick = onClick,
        modifier = modifier
    ) {
        Text(text = text)
    }
}

@Composable
fun StyledDefaultButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(corner = CornerSize(20.dp)),
        colors = ButtonColors(
            contentColor = Color.Blue,
            containerColor = Color.White,
            disabledContentColor = Color.Blue,
            disabledContainerColor = Color.White
        ),
        border = BorderStroke(
            width = 1.dp,
            color = Color.Blue
        ),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 10.dp),
        modifier = modifier
    ) {
        Text(
            text = text,
            fontWeight = FontWeight.W400,
            fontFamily = interFontFamily,
            fontSize = 12.sp
        )
    }
}

@Preview(showBackground = true)
@Composable
fun StyledDefaultButtonPreview() {
    val bg = Color.hsl(231F, 0.38F, 0.93F, 1F)

    Box(
        modifier = Modifier
            .width(300.dp)
            .height(300.dp)
            .background(bg),
        contentAlignment = Alignment.Center
    ) {
        StyledDefaultButton(text = "some text", onClick = { })
    }

}