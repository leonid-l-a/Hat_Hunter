package ru.practicum.android.diploma.vacancy.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.core.ui.theme.red
import ru.practicum.android.diploma.vacancy.ui.state.VacancyState
import ru.practicum.android.diploma.vacancy.ui.viewmodel.VacancyViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(viewModel: VacancyViewModel, navController: NavController, state: VacancyState) {
    val context = LocalContext.current
    TopAppBar(
        title = {
            Text(
                stringResource(R.string.vacancy_title),
                style = MaterialTheme.typography.headlineMedium,
            )
        },
        navigationIcon = {
            IconButton(onClick = {
                navController.popBackStack()
            }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(R.string.back))
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background,
        ),
        actions = {
            Row {
                IconButton(onClick = {
                    viewModel.shareVacancyWithMessenger()?.let { intent ->
                        context.startActivity(intent)
                    }
                }) {
                    Icon(
                        painter = painterResource(R.drawable.ic_share),
                        contentDescription = stringResource(R.string.share),
                    )
                }
                IconButton(onClick = {
                    viewModel.toggleFavorite()
                }) {
                    Like(state)
                }
            }
        }
    )
}

@Composable
fun Like(state: VacancyState) {
    when (state) {
        is VacancyState.Success -> {
            when (state.isFavorite) {
                true -> {
                    Icon(
                        painter = painterResource(R.drawable.ic_favorites_on),
                        contentDescription = stringResource(R.string.favorite_title),
                        tint = red
                    )
                }

                false -> {
                    Icon(
                        painter = painterResource(R.drawable.ic_favorites_off),
                        contentDescription = stringResource(R.string.favorite_title),
                    )
                }
            }
        }

        else -> null
    }
}
