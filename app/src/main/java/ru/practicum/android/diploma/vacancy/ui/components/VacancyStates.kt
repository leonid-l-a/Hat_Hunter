package ru.practicum.android.diploma.vacancy.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.vacancy.domain.model.VacancyDetail
import ru.practicum.android.diploma.vacancy.ui.viewmodel.VacancyViewModel

@Composable
fun ServerErrorState() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(R.drawable.ph_server_error_vacancy_screen),
            contentDescription = stringResource(R.string.server_error)
        )
    }
}

@Composable
fun ErrorState() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(R.drawable.ph_error_vacancy_screen),
                contentDescription = stringResource(R.string.error)
            )
            HorizontalDivider(thickness = 16.dp, color = Color.Transparent)
            Text(
                text = stringResource(R.string.vacancy_error),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.headlineMedium
            )
        }
    }
}

@Composable
fun SuccessState(vacancy: VacancyDetail, viewModel: VacancyViewModel) {
    VacancyHeader(vacancy)
    EmployerCard(vacancy)
    ExperienceSection(vacancy)
    DescriptionSection(vacancy, viewModel)
}

@Composable
fun LoadingState() {
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        CircularProgressIndicator()
    }
}
