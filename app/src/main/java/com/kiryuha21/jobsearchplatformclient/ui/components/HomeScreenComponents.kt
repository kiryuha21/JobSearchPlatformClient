package com.kiryuha21.jobsearchplatformclient.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kiryuha21.jobsearchplatformclient.R
import com.kiryuha21.jobsearchplatformclient.data.domain.CurrentUser
import com.kiryuha21.jobsearchplatformclient.ui.navigation.navigationDrawerItems
import kotlinx.coroutines.launch

// TODO: make real user info loading
@Composable
fun DrawerMiniProfile(login: String, modifier: Modifier = Modifier) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.padding(start=15.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.default_avatar),
            contentDescription = "default avatar",
            modifier = Modifier
                .size(120.dp)
        )
        Text("Hello, $login!")
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
                    Icon(imageVector = Icons.AutoMirrored.Filled.Logout, contentDescription = "log out")
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
                DrawerMiniProfile(CurrentUser.userInfo!!.login)
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
                            selectedItemIndex = index
                            scope.launch {
                                drawerState.close()
                            }
                            navigateFunction(item.navigationRoute)
                        })
                }

                val logOutIndex = navigationDrawerItems.size
                LogOutNavigationItem(
                    selected = selectedItemIndex == logOutIndex,
                    onClick = onLogOut
                )
            }
        },
        drawerState = drawerState,
        gesturesEnabled = true,
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

