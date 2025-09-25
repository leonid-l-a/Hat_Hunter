package ru.practicum.android.diploma.vacancy.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.core.ui.theme.lightGray
import ru.practicum.android.diploma.core.ui.theme.whiteNight
import ru.practicum.android.diploma.vacancy.domain.model.VacancyDetail

@Composable
fun EmployerCard(vacancy: VacancyDetail) {
    Card(
        modifier = Modifier
            .height(80.dp)
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = lightGray)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = vacancy.employerLogo,
                contentDescription = stringResource(R.string.employer_image),
                modifier = Modifier.size(48.dp),
                placeholder = painterResource(R.drawable.ic_employer_logo_placeholder),
            )

            Spacer(modifier = Modifier.width(8.dp))

            Column(
                modifier = Modifier.height(48.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = vacancy.employerName,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = whiteNight
                )
                Text(
                    text = vacancy.fullAddress
                        .takeIf { it.isNotBlank() }
                        ?: vacancy.areaName,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = whiteNight
                )

            }
        }
    }

    HorizontalDivider(thickness = 24.dp, color = Color.Transparent)
}
