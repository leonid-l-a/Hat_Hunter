package ru.practicum.android.diploma.favorites.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.network.NetworkHeaders
import coil3.network.httpHeaders
import coil3.request.ImageRequest
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.core.ui.theme.BorderWidth1
import ru.practicum.android.diploma.core.ui.theme.WrapperPaddingHorizontal16
import ru.practicum.android.diploma.core.ui.theme.WrapperPaddingVertical8
import ru.practicum.android.diploma.core.ui.theme.lightGray
import ru.practicum.android.diploma.favorites.domain.model.VacancyFavorites
import ru.practicum.android.diploma.favorites.utils.formatSalary

@Composable
fun FavoritesVacancyItem(
    vacancy: VacancyFavorites,
    onClick: (String) -> Unit = {}
) {
    Row(
        modifier = Modifier
            .padding(vertical = WrapperPaddingVertical8)
            .fillMaxWidth()
            .clickable { onClick(vacancy.id) },
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        AsyncImage(
            modifier = Modifier
                .size(48.dp)
                .clip(MaterialTheme.shapes.medium)
                .border(
                    BorderStroke(BorderWidth1, lightGray),
                    MaterialTheme.shapes.medium
                ),
            model = ImageRequest.Builder(LocalContext.current)
                .data(vacancy.employerLogo)
                .httpHeaders(NetworkHeaders.Builder().add("User-Agent", "Mozilla/5.0").build())
                .build(),
            placeholder = painterResource(R.drawable.item_placeholder),
            contentDescription = vacancy.name,
        )
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "${vacancy.name}, ${vacancy.addressCity}",
                style = MaterialTheme.typography.titleLarge
            )
            Text(
                text = vacancy.employerName,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.W400),
            )
            Text(
                text = formatSalary(vacancy.salaryFrom, vacancy.salaryTo, vacancy.salaryCurrency),
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.W400),
            )
        }
    }
}

@Composable
fun ShowFavoritesList(
    favoritesList: List<VacancyFavorites>,
    onClick: (String) -> Unit = {}
) {
    LazyColumn(
        modifier = Modifier
            .padding(horizontal = WrapperPaddingHorizontal16)
            .fillMaxSize()
    ) {
        items(favoritesList) {
            FavoritesVacancyItem(vacancy = it, onClick = onClick)
        }
    }
}
