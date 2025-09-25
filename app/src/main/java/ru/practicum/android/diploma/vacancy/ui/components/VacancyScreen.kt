package ru.practicum.android.diploma.vacancy.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.util.getCurrencySymbolByDigitId
import ru.practicum.android.diploma.vacancy.domain.model.VacancyDetail
import ru.practicum.android.diploma.vacancy.ui.state.VacancyState
import ru.practicum.android.diploma.vacancy.ui.viewmodel.VacancyViewModel

@Composable
fun VacancyScreen(viewModel: VacancyViewModel, navController: NavController) {
    val horizontalPadding = 16.dp
    val state by viewModel.state.collectAsState()

    Scaffold(
        topBar = { TopBar(viewModel, navController, state) }
    ) { paddingValues ->
        val columnModifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(horizontal = horizontalPadding)

        Column(
            modifier = if (state is VacancyState.Success) {
                columnModifier.verticalScroll(rememberScrollState())
            } else {
                columnModifier
            }
        ) {
            when (state) {
                is VacancyState.Loading -> LoadingState()
                is VacancyState.ServerError -> ServerErrorState()
                is VacancyState.Error -> ErrorState()
                is VacancyState.Success -> {
                    val vacancyDetail = (state as VacancyState.Success).vacancyDetail
                    SuccessState(vacancyDetail, viewModel)
                }
            }
        }
    }
}
fun VacancyDetail.getSalaryString(): String {
    return when {
        salaryFrom != null && salaryTo != null ->
            "от $salaryFrom до $salaryTo ${salaryCurrency.getCurrencySymbolByDigitId()}"
        salaryFrom != null ->
            "от $salaryFrom ${salaryCurrency.getCurrencySymbolByDigitId()}"
        salaryTo != null ->
            "до $salaryTo ${salaryCurrency.getCurrencySymbolByDigitId()}"
        else ->
            R.string.salary_not_specified.toString()
    }
}
