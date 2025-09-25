package ru.practicum.android.diploma.filtration.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.core.ui.components.CircularIndicator
import ru.practicum.android.diploma.core.ui.components.FilterItem
import ru.practicum.android.diploma.filtration.ui.components.SearchFilterItems
import ru.practicum.android.diploma.filtration.ui.components.ShowEmptyRegion
import ru.practicum.android.diploma.filtration.ui.components.ShowErrorRegion
import ru.practicum.android.diploma.filtration.ui.components.TopBar
import ru.practicum.android.diploma.filtration.ui.state.RegionSelectionScreenState
import ru.practicum.android.diploma.filtration.ui.viewmodel.RegionSelectionViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegionSelectionScreen(
    viewModel: RegionSelectionViewModel,
    navController: NavController,
    currentCountryId: Int?,
) {
    val screenState by viewModel.screenState.collectAsState()
    val (searchText, setSearchText) = remember { mutableStateOf("") }

    LaunchedEffect(currentCountryId) {
        viewModel.loadData(currentCountryId)
    }

    Scaffold(
        topBar = {
            TopBar(
                stringResource(R.string.region_selector)
            ) { navController.popBackStack() }
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier.padding(paddingValues)
        ) {
            SearchFilterItems(
                text = searchText,
                placeholder = stringResource(R.string.search_region_placeholder),
                onSearchHandler = { query ->
                    setSearchText(query)
                    viewModel.onSearchQueryChanged(query)
                },
                onResetRequest = {
                    setSearchText("")
                    viewModel.onSearchQueryChanged("")
                }
            )

            when (val state = screenState) {
                is RegionSelectionScreenState.Data -> {
                    LazyColumn(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        items(state.filteredRegions) { region ->
                            FilterItem(
                                labelText = region.name,
                                checked = false,
                                onClick = {
                                    viewModel.onRegionClicked(region, navController)
                                }
                            ) { checked ->
                                val resId = if (checked) {
                                    R.drawable.close_24
                                } else {
                                    R.drawable.arrow_forward_24
                                }
                                Icon(
                                    tint = MaterialTheme.colorScheme.onBackground,
                                    painter = painterResource(id = resId),
                                    contentDescription = null
                                )
                            }
                        }
                    }
                }

                RegionSelectionScreenState.Error -> {
                    Column(
                        modifier = Modifier
                            .fillMaxHeight()
                            .weight(1f),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        ShowErrorRegion()
                    }
                }

                RegionSelectionScreenState.Loading -> {
                    Column(
                        modifier = Modifier
                            .padding(paddingValues)
                            .fillMaxHeight(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularIndicator()
                    }
                }

                RegionSelectionScreenState.EmptyResult -> {
                    ShowEmptyRegion()
                }
            }
        }
    }
}
