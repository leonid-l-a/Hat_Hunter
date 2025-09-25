package ru.practicum.android.diploma.filtration.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.core.ui.components.FilterItem
import ru.practicum.android.diploma.core.ui.theme.ApplicationTheme

@Composable
private fun ActionIcon(checked: Boolean, modifier: Modifier = Modifier) {
    val resId = if (checked) R.drawable.close_24 else R.drawable.arrow_forward_24
    Icon(
        painter = painterResource(id = resId),
        contentDescription = null,
        tint = MaterialTheme.colorScheme.onBackground,
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun WorkPlaceTopBar(navController: NavController) {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent
        ),
        title = {
            Text(
                text = stringResource(R.string.work_place_title),
                style = MaterialTheme.typography.headlineMedium
            )
        },
        navigationIcon = {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(R.string.back)
                )
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkPlaceSelector(
    countryLabel: String?,
    regionLabel: String?,
    countryChecked: Boolean,
    regionChecked: Boolean,
    onCountryClick: () -> Unit,
    onRegionClick: () -> Unit,
    onCountryClear: () -> Unit,
    onRegionClear: () -> Unit,
    navController: NavController,
    bottomBar: @Composable (() -> Unit),
) {
    Scaffold(
        topBar = { WorkPlaceTopBar(navController) },
        bottomBar = bottomBar
    ) { paddingValues ->
        Column(
            modifier = Modifier.padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            WorkPlaceFilterItem(
                text = stringResource(R.string.country),
                value = countryLabel ?: "",
                checked = countryChecked,
                onClick = { onCountryClick() },
                onClear = onCountryClear
            )

            WorkPlaceFilterItem(
                text = stringResource(R.string.region),
                value = regionLabel ?: "",
                checked = regionChecked,
                onClick = { onRegionClick() },
                onClear = onRegionClear
            )
        }
    }
}

@Composable
private fun WorkPlaceFilterItem(
    text: String,
    value: String,
    checked: Boolean,
    onClick: (String?) -> Unit,
    onClear: () -> Unit
) {
    FilterItem(
        labelText = text,
        labelValue = value,
        checked = checked,
        isMainField = true,
        onClick = onClick,
        onClear = onClear
    ) { isChecked ->
        ActionIcon(isChecked)
    }
}

@Preview
@Composable
fun WorkPlacePreview() {
    ApplicationTheme {
        WorkPlaceSelector(
            countryLabel = null,
            regionLabel = null,
            countryChecked = true,
            regionChecked = true,
            onCountryClick = {},
            onRegionClick = {},
            onCountryClear = {},
            onRegionClear = {},
            navController = rememberNavController()
        ) { }
    }
}
