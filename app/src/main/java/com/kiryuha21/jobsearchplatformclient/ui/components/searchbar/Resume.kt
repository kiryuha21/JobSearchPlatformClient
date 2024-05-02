package com.kiryuha21.jobsearchplatformclient.ui.components.searchbar

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.kiryuha21.jobsearchplatformclient.data.domain.filters.ResumeFilters

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResumeSearchBar(
    onSearch: (ResumeFilters) -> Unit,
    modifier: Modifier = Modifier
) {
    var currentQuery by remember { mutableStateOf("") }
    var bodyVisible by remember { mutableStateOf(false) }
    var resumeFilters by remember { mutableStateOf(ResumeFilters()) }

    SearchBar(
        query = currentQuery,
        onQueryChange = {
            currentQuery = it
            resumeFilters = resumeFilters.copy(applyPosition = it)
        },
        onSearch = { onSearch(resumeFilters) },
        active = bodyVisible,
        onActiveChange = { bodyVisible = !bodyVisible },
        leadingIcon = {
            Icon(imageVector = Icons.Default.Search, contentDescription = "search")
        },
        trailingIcon = {
            if (bodyVisible) {
                IconButton(
                    onClick = {
                        if (currentQuery.isEmpty()) {
                            bodyVisible = false
                        } else {
                            currentQuery = ""
                        }
                    }
                ) {
                    Icon(imageVector = Icons.Default.Clear, contentDescription = "clear")
                }
            }
        },
        modifier = modifier
    ) {
        Text(text = "filters here...")
    }
}

@Preview
@Composable
fun ResumeSearchBarPreview() {
    ResumeSearchBar(onSearch = {})
}