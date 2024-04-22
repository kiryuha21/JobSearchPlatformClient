package com.kiryuha21.jobsearchplatformclient.ui.navigation

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.kiryuha21.jobsearchplatformclient.R
import com.kiryuha21.jobsearchplatformclient.data.domain.CurrentUser
import com.kiryuha21.jobsearchplatformclient.ui.components.Title
import com.kiryuha21.jobsearchplatformclient.util.getBitmap
import kotlinx.coroutines.launch

@Composable
fun DrawerMiniProfile(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    var selectedImageUri by remember {
        mutableStateOf(if (CurrentUser.info.imageUrl != null) Uri.parse(CurrentUser.info.imageUrl) else null)
    }
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) {
        if (it != null) {
            context.contentResolver.takePersistableUriPermission(it, Intent.FLAG_GRANT_READ_URI_PERMISSION)
            selectedImageUri = it
            CallbacksRegistry.setProfileImageCallback.value(it.getBitmap(context))
        }
    }

    val painter = if (selectedImageUri != null) {
        rememberAsyncImagePainter(
            model = ImageRequest.Builder(LocalContext.current)
                .data(selectedImageUri)
                .crossfade(true)
                .build()
        )
    } else {
        painterResource(id = R.drawable.upload_photo)
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.padding(start = 15.dp, top = 10.dp)
    ) {
        Image(
            painter = painter,
            contentDescription = "upload_photo",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(128.dp)
                .clip(RoundedCornerShape(10.dp))
                .border(2.dp, Color.Gray, RoundedCornerShape(10.dp))
                .clickable {
                    photoPickerLauncher.launch(
                        PickVisualMediaRequest(
                            ActivityResultContracts.PickVisualMedia.ImageOnly
                        )
                    )
                }
        )
        Text("Привет, ${CurrentUser.info.username}!")
    }
}

@Composable
fun LogOutNavigationItem(selected: Boolean, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxHeight()
            .padding(bottom = 20.dp),
        contentAlignment = Alignment.BottomStart
    ) {
        NavigationDrawerItem(
            label = {
                Row {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Logout,
                        contentDescription = "log out"
                    )
                    Text(text = "Выйти")
                }
            },
            selected = selected,
            onClick = onClick
        )
    }

}

@Composable
fun NavigationDrawer(
    navigateFunction: (String) -> Unit,
    onLogOut: () -> Unit,
    gesturesEnabled: Boolean,
    modifier: Modifier = Modifier,
    content: @Composable (() -> Unit) -> Unit
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var selectedItemIndex by rememberSaveable {
        mutableIntStateOf(0)
    }

    ModalNavigationDrawer(
        drawerContent = {
            ModalDrawerSheet {
                DrawerMiniProfile()
                Spacer(modifier = Modifier.height(20.dp))
                navigationDrawerItems.forEachIndexed { index, item ->
                    NavigationDrawerItem(
                        label = {
                            Row {
                                Icon(imageVector = item.icon, contentDescription = item.text)
                                Text(text = item.text)
                            }
                        },
                        selected = index == selectedItemIndex,
                        onClick = {
                            if (index != selectedItemIndex) {
                                selectedItemIndex = index
                                navigateFunction(item.navigationRoute)
                            }

                            scope.launch {
                                drawerState.close()
                            }
                        },
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                    )
                }

                val logOutIndex = navigationDrawerItems.size
                LogOutNavigationItem(
                    selected = selectedItemIndex == logOutIndex,
                    onClick = {
                        scope.launch {
                            drawerState.close()
                            onLogOut()
                        }
                    }
                )
            }
        },
        drawerState = drawerState,
        gesturesEnabled = gesturesEnabled,
        modifier = modifier,
        content = {
            content {
                scope.launch {
                    drawerState.open()
                }
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar(onClick: () -> Unit, modifier: Modifier = Modifier) {
    TopAppBar(
        title = {
            val context = LocalContext.current
            Title(
                text = context.getString(R.string.app_name),
                fontSize = 20.sp
            )
        },
        navigationIcon = {
            IconButton(onClick = onClick) {
                Icon(imageVector = Icons.Rounded.Menu, contentDescription = "menu")
            }
        },
        modifier = modifier
    )
}

@Composable
fun MainAppScaffold(
    navigateFunction: (String) -> Unit,
    onLogOut: () -> Unit,
    shouldShowTopBar: Boolean,
    content: @Composable (PaddingValues) -> Unit
) {
    NavigationDrawer(
        navigateFunction = navigateFunction,
        onLogOut = onLogOut,
        gesturesEnabled = shouldShowTopBar
    ) { onOpenDrawer ->
        Scaffold(
            topBar = {
                if (shouldShowTopBar) {
                    AppBar(onClick = onOpenDrawer)
                }
            },
            content = content
        )
    }
}

