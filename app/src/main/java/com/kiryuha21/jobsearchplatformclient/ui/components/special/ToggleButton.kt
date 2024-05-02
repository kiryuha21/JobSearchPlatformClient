package com.kiryuha21.jobsearchplatformclient.ui.components.special

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kiryuha21.jobsearchplatformclient.data.domain.UserRole

data class ToggleButtonElement(
    val role: UserRole,
    val text: String
)

val roleToggleItems = listOf(
    ToggleButtonElement(UserRole.Worker, "Работник"),
    ToggleButtonElement(UserRole.Employer, "Работодатель")
)

@Composable
fun MultiToggleButton(
    toggleStates: List<ToggleButtonElement>,
    modifier: Modifier = Modifier,
    onToggleChange: (ToggleButtonElement) -> Unit
) {
    var selectedIndex by rememberSaveable { mutableIntStateOf(0) }
    val selectedTint = MaterialTheme.colorScheme.primary
    val unselectedTint = Color.Unspecified

    Row(
        modifier = modifier
            .height(IntrinsicSize.Min)
            .border(BorderStroke(1.dp, Color.LightGray))
    ) {
        toggleStates.forEachIndexed { index, toggleValue ->
            val isSelected = selectedIndex == index
            val backgroundTint = if (isSelected) selectedTint else unselectedTint
            val textColor = if (isSelected) Color.White else Color.Unspecified

            if (index != 0) {
                VerticalDivider(
                    thickness = 1.dp,
                    modifier = Modifier.fillMaxHeight(),
                    color = Color.LightGray
                )
            }

            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .background(backgroundTint)
                    .fillMaxWidth()
                    .weight(1F, false)
                    .padding(vertical = 6.dp, horizontal = 8.dp)
                    .toggleable(
                        value = isSelected,
                        role = Role.Switch,
                        onValueChange = { selected ->
                            if (selected) {
                                selectedIndex = index
                                onToggleChange(toggleValue)
                            }
                        })
            ) {
                Text(
                    toggleValue.text.uppercase(),
                    color = textColor,
                    modifier = Modifier.padding(4.dp)
                )
            }

        }
    }
}

@Preview(showBackground = true)
@Composable
fun ToggleButtonPreview() {
    MultiToggleButton(toggleStates = roleToggleItems) {}
}