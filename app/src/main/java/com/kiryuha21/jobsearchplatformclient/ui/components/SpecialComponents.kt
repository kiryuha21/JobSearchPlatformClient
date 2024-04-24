package com.kiryuha21.jobsearchplatformclient.ui.components

import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.kiryuha21.jobsearchplatformclient.R
import kotlinx.coroutines.android.awaitFrame
import kotlinx.coroutines.launch

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

@Composable
fun ClickableAsyncUriImage(
    selectedImageUri: Uri?,
    defaultResourceId: Int,
    onClick: () -> Unit
) {
    val painter = if (selectedImageUri != null) {
        rememberAsyncImagePainter(
            model = ImageRequest.Builder(LocalContext.current)
                .data(selectedImageUri)
                .crossfade(true)
                .build()
        )
    } else {
        painterResource(id = defaultResourceId)
    }

    Image(
        painter = painter,
        contentDescription = "upload_photo",
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .size(128.dp)
            .clip(RoundedCornerShape(10.dp))
            .border(2.dp, Color.Gray, RoundedCornerShape(10.dp))
            .clickable { onClick() }
    )
}

@Composable
fun OnBackPressedWithSuper(
    onBackPressed: () -> Unit
) {
    val onBackPressedDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher
    var backPressHandled by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    BackHandler(enabled = !backPressHandled) {
        onBackPressed()
        backPressHandled = true
        coroutineScope.launch {
            awaitFrame()
            onBackPressedDispatcher?.onBackPressed()
            backPressHandled = false
        }
    }
}
