package com.kiryuha21.jobsearchplatformclient.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun NoItemsCard(modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.height(100.dp),
        shape = RoundedCornerShape(20.dp)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text="Сейчас здесь ничего нет", fontSize = 18.sp)
                Text(text = "Может, стоит обновить страницу?", fontSize = 10.sp)
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun NoItemsCardPreview() {
    NoItemsCard(modifier = Modifier.fillMaxWidth())
}

data class ComboBoxItem(
    val text: String,
    val onClick: () -> Unit
)

@Composable
fun ComboBox(items: List<ComboBoxItem>, modifier: Modifier = Modifier) {
    var currentIndex by remember { mutableIntStateOf(0) }
    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
    ) {
        TextButton(onClick = { expanded = !expanded }) {
            Text(text = items[currentIndex].text)
            Icon(imageVector = Icons.Default.KeyboardArrowDown, contentDescription = "Options")
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            items.forEachIndexed { index, item ->
                DropdownMenuItem(
                    text = { Text(text = item.text) },
                    onClick = {
                        currentIndex = index
                        expanded = false
                        item.onClick()
                    }
                )
            }
        }
    }
}
