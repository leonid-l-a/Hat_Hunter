package ru.practicum.android.diploma.main.ui

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import org.koin.androidx.compose.koinViewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.core.navigation.Screen
import ru.practicum.android.diploma.core.ui.components.CircularIndicator
import ru.practicum.android.diploma.core.ui.components.ErrorResult
import ru.practicum.android.diploma.core.ui.components.SearchBar
import ru.practicum.android.diploma.core.ui.theme.ApplicationTheme
import ru.practicum.android.diploma.core.ui.theme.WidthForInfoImage328
import ru.practicum.android.diploma.core.ui.theme.blackUniversal
import ru.practicum.android.diploma.main.ui.components.SearchCount
import ru.practicum.android.diploma.main.ui.components.ShowVacancyList
import ru.practicum.android.diploma.main.ui.components.VacancyAppBar
import ru.practicum.android.diploma.main.ui.state.SearchState
import ru.practicum.android.diploma.main.ui.viewmodel.SearchVacancyViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: SearchVacancyViewModel = koinViewModel(),
    onVacancyClick: (String) -> Unit = {}
) {
    Scaffold(
        topBar = {
            VacancyAppBar(
                viewModel = viewModel,
                modifier = Modifier,
                onClick = {
                    navController.navigate(Screen.Filtration.route)
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            SearchVacancies(
                modifier = Modifier,
                onSearchHandler = { text ->
                    viewModel.searchRequestText(expression = text)
                },
                onResetRequest = {
                    viewModel.renderDefaultState()
                }
            )
            ShowContent(viewModel = viewModel) {
                onVacancyClick(it)
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SearchScreenPreview() {
    ApplicationTheme {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            SearchVacancies(modifier = Modifier)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1F),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    modifier = Modifier.width(WidthForInfoImage328),
                    contentScale = ContentScale.Crop,
                    painter = painterResource(R.drawable.start_search),
                    contentDescription = null
                )
            }
        }
    }
}

@Composable
fun SearchVacancies(
    modifier: Modifier = Modifier,
    onSearchHandler: (String) -> Unit = {},
    onResetRequest: () -> Unit = {}
) {
    var text by rememberSaveable { mutableStateOf("") }
    SearchBar(
        modifier = modifier,
        value = text,
        onValueChange = { newText ->
            text = newText
            if (text.isNotEmpty()) {
                onSearchHandler(text)
            } else {
                onResetRequest()
            }
        },
        placeholder = {
            Text(
                color = MaterialTheme.colorScheme.onTertiary,
                text = stringResource(R.string.search_bar_placeholder),
                style = MaterialTheme.typography.bodyLarge,
            )
        },
        trailingIcon = {
            val ico = if (text.isEmpty()) R.drawable.ic_search else R.drawable.ic_close
            Icon(
                tint = blackUniversal,
                modifier = Modifier.clickable {
                    if (text.isNotEmpty()) {
                        text = ""
                        onResetRequest()
                    }
                },
                imageVector = ImageVector.vectorResource(id = ico),
                contentDescription = null
            )
        }
    )
}

@Composable
private fun ShowContent(
    viewModel: SearchVacancyViewModel,
    onVacancyClick: (String) -> Unit = {}
) {
    val searchState by viewModel.stateSearchVacancy.collectAsState()
    val shouldRepeatRequest by viewModel.shouldRepeatRequest.collectAsState()
    var errorWasShown by remember { mutableStateOf(false) }
    val context = LocalContext.current
    LaunchedEffect(shouldRepeatRequest) {
        if (shouldRepeatRequest) {
            viewModel.repeatRequest()
            viewModel.setShouldRepeatRequest(shouldRepeat = false)
        }
    }
    LaunchedEffect(searchState) {
        if (searchState is SearchState.Content) {
            errorWasShown = false
        }
    }
    LaunchedEffect(key1 = (searchState as? SearchState.Content)?.isLazyError) {
        if (searchState is SearchState.Content) {
            val stateContent = searchState as SearchState.Content
            if (stateContent.isLazyError && !errorWasShown) {
                Toast.makeText(
                    context,
                    context.getString(R.string.check_internet),
                    Toast.LENGTH_SHORT
                ).show()
                errorWasShown = true
            }
        }
    }
    when (searchState) {
        is SearchState.Empty -> {
            SearchCount(text = stringResource(id = R.string.no_such_vacancies))
            ErrorResult(
                textRes = R.string.favorites_is_error,
                painterRes = R.drawable.favorites_error
            )
        }

        is SearchState.Error -> {

            ErrorResult(
                textRes = R.string.no_connection,
                painterRes = R.drawable.no_connection
            )
        }

        is SearchState.Default -> {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    modifier = Modifier.width(328.dp),
                    contentScale = ContentScale.Crop,
                    painter = painterResource(R.drawable.start_search),
                    contentDescription = null
                )
            }
        }

        is SearchState.Loading -> CircularIndicator()

        is SearchState.Content -> {
            val currentState = searchState
            if (currentState is SearchState.Content) {
                if (currentState.vacancy.isEmpty()) {
                    return
                }
                val searchCountText = if (currentState.countVacancy != null) {
                    pluralStringResource(
                        R.plurals.items_count,
                        currentState.countVacancy,
                        currentState.countVacancy
                    )
                } else {
                    stringResource(R.string.no_such_vacancies)
                }
                SearchCount(text = searchCountText)
                ShowVacancyList(
                    vacancyList = currentState.vacancy,
                    isLoadingNextPage = currentState.isLoadingNextPage,
                    onClick = onVacancyClick,
                    onLoadNextPage = {
                        viewModel.showMore()
                    },
                )
            }
        }
    }
}
