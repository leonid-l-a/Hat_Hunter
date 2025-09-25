package ru.practicum.android.diploma.vacancy.ui.components

import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.vacancy.domain.model.VacancyDetail
import ru.practicum.android.diploma.vacancy.ui.viewmodel.VacancyViewModel

@Composable
fun ContactItem(
    contact: String,
    isEmail: Boolean,
    viewModel: VacancyViewModel,
) {
    val context = LocalContext.current

    Text(
        text = contact,
        modifier = Modifier.clickable {
            val intent = if (isEmail) {
                viewModel.writeWithMail()
            } else {
                viewModel.callWithPhone()
            }
            intent?.let { context.startActivity(it) }
        }
    )
}

@Composable
fun VacancyDescriptionItem(title: String, description: List<String>) {
    Column {
        Text(
            text = title,
            style = MaterialTheme.typography.headlineSmall,
        )

        HorizontalDivider(thickness = 4.dp, color = Color.Transparent)

        for (item in description) {
            Text(
                text = " • $item",
                style = MaterialTheme.typography.bodyLarge,
            )
        }

        HorizontalDivider(thickness = 12.dp, color = Color.Transparent)
    }
}
fun createDescriptionItemsList(context: Context, vacancy: VacancyDetail): List<Pair<String, List<String>>> {
    return listOf(
        context.getString(R.string.schedule) to listOfNotNull(vacancy.scheduleName),
        context.getString(R.string.skills) to vacancy.skills,
        context.getString(R.string.industry) to listOfNotNull(vacancy.industryName),
        context.getString(R.string.address) to listOfNotNull(
            listOfNotNull(
                vacancy.addressCity,
                vacancy.addressStreet,
                vacancy.addressBuilding
            ).takeIf { it.isNotEmpty() }?.joinToString(", ")
        ),
        context.getString(R.string.description) to vacancy.description
            .split(Regex("<br>|<br/>|<br />"))
            .map { it.trim() }
            .filter { it.isNotEmpty() }
    )
}

fun createContactItemsList(vacancy: VacancyDetail): List<Triple<String, Boolean?, Boolean>> {
    return listOfNotNull(
        vacancy.contactsEmail.takeIf { it.isNotBlank() }?.let { Triple(it, true, true) },
        vacancy.contactsPhone?.firstOrNull()?.let { Triple(it, false, true) },
        Triple(vacancy.contactsName, null, false)
    )
}
