package ru.practicum.android.diploma.filtration.ui.screens

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.core.navigation.Screen
import ru.practicum.android.diploma.core.ui.components.FilterButton
import ru.practicum.android.diploma.core.ui.theme.Height24
import ru.practicum.android.diploma.core.ui.theme.Height60
import ru.practicum.android.diploma.core.ui.theme.WrapperPaddingHorizontal16
import ru.practicum.android.diploma.filtration.ui.components.WorkPlaceSelector
import ru.practicum.android.diploma.filtration.ui.viewmodel.WorkPlaceViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkPlaceScreen(
    viewModel: WorkPlaceViewModel,
    navController: NavController
) {
    val countryName by viewModel.countryName.collectAsState()
    val regionName by viewModel.regionName.collectAsState()
    val countryId by viewModel.countryId.collectAsState()
    val regionId by viewModel.regionId.collectAsState()

    val countryChecked = countryName.isNotBlank()
    val regionChecked = regionName.isNotBlank()

    WorkPlaceSelector(
        countryLabel = countryName.ifBlank { null },
        regionLabel = regionName.ifBlank { null },
        countryChecked = countryChecked,
        regionChecked = regionChecked,
        onCountryClick = {
            if (countryName.isBlank()) {
                navController.navigate(Screen.CountrySelection.route)
            }
        },
        onRegionClick = {
            if (regionName.isBlank()) {
                val currentAreaId = if (regionId.isNullOrBlank()) {
                    countryId?.toIntOrNull()
                } else {
                    regionId?.toIntOrNull()
                }
                navController.navigate(Screen.RegionSelection.regionSelectionRoute(currentAreaId))
            }
        },
        onCountryClear = {
            viewModel.clearCountry()
        },
        onRegionClear = {
            viewModel.clearRegion()
        },
        navController = navController,
        {
            if (countryChecked || regionChecked) {
                FilterButton(
                    modifier = Modifier
                        .height(Height60)
                        .padding(horizontal = WrapperPaddingHorizontal16)
                        .offset(y = -Height24)
                        .fillMaxWidth(),
                    textButton = stringResource(R.string.filter_apply),
                    onClick = { navController.popBackStack() }
                )
            }
        }
    )
}
