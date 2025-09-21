package ru.practicum.android.diploma.filtration.ui.screens

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import org.koin.androidx.compose.koinViewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.core.ui.components.CircularIndicator
import ru.practicum.android.diploma.core.ui.components.ErrorResult
import ru.practicum.android.diploma.core.ui.components.FilterButton
import ru.practicum.android.diploma.core.ui.components.FilterItem
import ru.practicum.android.diploma.core.ui.components.FilterParams
import ru.practicum.android.diploma.core.ui.theme.ApplicationTheme
import ru.practicum.android.diploma.core.ui.theme.Height24
import ru.practicum.android.diploma.core.ui.theme.Height60
import ru.practicum.android.diploma.core.ui.theme.WrapperPaddingHorizontal16
import ru.practicum.android.diploma.core.ui.theme.blue
import ru.practicum.android.diploma.filtration.ui.components.SearchFilterItems
import ru.practicum.android.diploma.filtration.ui.components.TopBar
import ru.practicum.android.diploma.filtration.ui.model.data.Industry
import ru.practicum.android.diploma.filtration.ui.state.IndustryState
import ru.practicum.android.diploma.filtration.ui.viewmodel.IndustryViewModel

@Composable
fun IndustryFilterScreen(
    modifier: Modifier = Modifier,
    industryId: String? = null,
    navController: NavController? = null,
    industryVm: IndustryViewModel = koinViewModel(),
) {
    Scaffold(
        topBar = {
            TopBar(
                text = stringResource(R.string.choose_industry),
                onBackNavigate = {
                    navController?.popBackStack()
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            var text by rememberSaveable { mutableStateOf("") }
            SearchFilterItems(
                text = text,
                placeholder = stringResource(R.string.search_industry_placeholder),
                onSearchHandler = {
                    text = it
                    industryVm.filerItems(text)
                },
                onResetRequest = {
                    text = ""
                    industryVm.filerItems(text)
                }
            )
            ShowContent(
                industryVm = industryVm,
                navController = navController,
                industryId = industryId
            )
        }
    }
}

@Composable
private fun ShowContent(
    modifier: Modifier = Modifier,
    industryVm: IndustryViewModel = koinViewModel(),
    navController: NavController? = null,
    industryId: String? = null,
) {
    val industryState by industryVm.industryState.collectAsState()
    var selectedValue by remember { mutableStateOf<Industry?>(null) }
    LaunchedEffect(industryId, industryState) {
        if (industryId?.isNotEmpty() == true && selectedValue == null) {
            val items = (industryState as? IndustryState.Content)?.items ?: emptyList()
            selectedValue = items.firstOrNull { it.id.toString() == industryId }
        }
    }
    when (industryState) {
        is IndustryState.Idle -> {}
        is IndustryState.Loading -> CircularIndicator()
        is IndustryState.Content -> {
            val items = (industryState as IndustryState.Content).items

            ShowItems(
                selectedValue = selectedValue,
                industryItems = items,
                onSelectionChanged = { selectedValue = it },
                navController = navController
            )
        }

        is IndustryState.EmptyResult -> ShowError(
            textRes = R.string.choose_industry_not_exist,
            painterRes = R.drawable.favorites_error
        )

        is IndustryState.ClientError -> ShowError(
            textRes = R.string.no_connection,
            painterRes = R.drawable.no_connection
        )

        is IndustryState.ServerError -> ShowError(
            textRes = R.string.server_error_text,
            painterRes = R.drawable.ph_server_error_vacancy_screen
        )
    }

}

@Composable
private fun ShowItems(
    onSelectionChanged: (Industry?) -> Unit,
    industryItems: List<Industry>,
    modifier: Modifier = Modifier,
    industryVm: IndustryViewModel = koinViewModel(),
    navController: NavController? = null,
    selectedValue: Industry? = null,
) {
    Column {
        LazyColumn(
            modifier = modifier
                .selectableGroup()
                .weight(1F)
        ) {
            items(items = industryItems) { radioItem ->
                FilterItem(
                    modifier = Modifier
                        .selectable(
                            selected = radioItem == selectedValue,
                            onClick = { onSelectionChanged(radioItem) },
                            role = Role.RadioButton
                        ),
                    labelText = radioItem.name,
                    fieldType = FilterParams.FIELDTYPE.RADIO_BUTTON,
                ) {
                    RadioButton(
                        onClick = null,
                        selected = radioItem == selectedValue,
                        colors = RadioButtonDefaults.colors(
                            selectedColor = blue,
                            unselectedColor = blue,
                        )
                    )
                }
            }
        }
        selectedValue?.let {
            FilterButton(
                modifier = Modifier
                    .padding(
                        bottom = Height24,
                        start = WrapperPaddingHorizontal16,
                        end = WrapperPaddingHorizontal16
                    )
                    .height(Height60)
                    .fillMaxWidth(),

                textButton = stringResource(R.string.filter_add),
                onClick = {
                    industryVm.saveIndustry(industry = selectedValue)
                    navController?.popBackStack()
                }
            )
        }
    }
}

@Composable
private fun ShowError(
    @StringRes textRes: Int,
    @DrawableRes painterRes: Int,
    modifier: Modifier = Modifier,
) {
    ErrorResult(
        textRes = textRes,
        painterRes = painterRes
    )
}

@Preview
@Composable
fun IndustryFilterPreview() {
    ApplicationTheme {
        IndustryFilterScreen()
    }
}
