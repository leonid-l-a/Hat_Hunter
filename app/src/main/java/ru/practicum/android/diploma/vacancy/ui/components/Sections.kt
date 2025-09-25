package ru.practicum.android.diploma.vacancy.ui.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.text.isDigitsOnly
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.vacancy.domain.model.VacancyDetail
import ru.practicum.android.diploma.vacancy.ui.viewmodel.VacancyViewModel

@Composable
fun VacancyHeader(vacancy: VacancyDetail) {
    HorizontalDivider(thickness = 16.dp, color = Color.Transparent)

    Text(
        text = vacancy.name,
        style = MaterialTheme.typography.headlineLarge
    )

    val salary = if (vacancy.getSalaryString().isDigitsOnly()) {
        stringResource(R.string.salary_not_specified)
    } else {
        vacancy.getSalaryString()
    }

    Text(
        text = salary,
        style = MaterialTheme.typography.headlineMedium
    )

    HorizontalDivider(thickness = 16.dp, color = Color.Transparent)
}

@Composable
fun ExperienceSection(vacancy: VacancyDetail) {
    Text(
        text = stringResource(R.string.required_experience),
        style = MaterialTheme.typography.headlineSmall
    )

    HorizontalDivider(thickness = 4.dp, color = Color.Transparent)

    Text(
        text = vacancy.experienceName,
        style = MaterialTheme.typography.bodyLarge
    )

    HorizontalDivider(thickness = 8.dp, color = Color.Transparent)

    Text(
        text = "${vacancy.employmentName}, ${stringResource(R.string.remote_work)}",
        style = MaterialTheme.typography.bodyLarge
    )

    HorizontalDivider(thickness = 32.dp, color = Color.Transparent)
}

@Composable
fun DescriptionSection(vacancy: VacancyDetail, viewModel: VacancyViewModel) {
    Text(
        text = stringResource(R.string.description_section),
        style = MaterialTheme.typography.headlineMedium
    )

    HorizontalDivider(thickness = 16.dp, color = Color.Transparent)

    val context = LocalContext.current
    val otherItems = createDescriptionItemsList(context, vacancy)
    val contactItems = createContactItemsList(vacancy)
    otherItems.forEach { (title, desc) ->
        if (desc.isNotEmpty()) {
            VacancyDescriptionItem(title = title, description = desc)
        }
    }

    ContactsSection(contactItems, viewModel)

    Spacer(modifier = Modifier.height(16.dp))
}

@Composable
private fun ContactsSection(contactItems: List<Triple<String, Boolean?, Boolean>>, viewModel: VacancyViewModel) {
    if (contactItems.isNotEmpty()) {
        Text(
            text = stringResource(R.string.contacts_section),
            style = MaterialTheme.typography.headlineSmall
        )

        HorizontalDivider(thickness = 4.dp, color = Color.Transparent)

        contactItems.forEach { (value, isEmail, _) ->
            when (isEmail) {
                true -> ContactItem(contact = value, isEmail = true, viewModel = viewModel)
                false -> ContactItem(contact = value, isEmail = false, viewModel = viewModel)
                else -> Text(text = value)
            }
        }
    }
}
