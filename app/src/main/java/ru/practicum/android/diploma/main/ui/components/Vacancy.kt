package ru.practicum.android.diploma.main.ui.components

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
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
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
import ru.practicum.android.diploma.main.ui.model.Vacancy

@Composable
fun VacancyItem(
    vacancy: Vacancy,
    modifier: Modifier = Modifier,
    onClick: (String) -> Unit = {}
) {
    Row(
        modifier = modifier
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
                .data(vacancy.logoUrl)
                .httpHeaders(NetworkHeaders.Builder().add("User-Agent", "Mozilla/5.0").build())
                .build(),
            placeholder = painterResource(R.drawable.item_placeholder),
            contentDescription = vacancy.name,
        )
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "${vacancy.name}, ${vacancy.city}",
                style = MaterialTheme.typography.titleLarge
            )
            Text(
                text = vacancy.industry,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.W400),
            )
            Text(
                text = vacancy.salary,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.W400),
            )
        }
    }
}

@Composable
fun ShowVacancyList(
    vacancyList: List<Vacancy>,
    modifier: Modifier = Modifier,
    isLoadingNextPage: Boolean = false,
    onClick: (String) -> Unit = {},
    onLoadNextPage: () -> Unit = {},
    diffCount: Int = 5,
    preserveOnEmpty: Boolean = false
) {
    if (vacancyList.isEmpty() && !preserveOnEmpty) {
        return
    }
    val dif = if (preserveOnEmpty) 1 else diffCount
    val listState = rememberLazyListState()

    val shouldLoadNext by remember {
        derivedStateOf {
            val lastVisibleItemIndex = listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index
            val totalItemsCount = listState.layoutInfo.totalItemsCount
            lastVisibleItemIndex != null && lastVisibleItemIndex >= totalItemsCount - dif && !isLoadingNextPage
        }
    }

    LaunchedEffect(shouldLoadNext) {
        if (shouldLoadNext) {
            onLoadNextPage()
        }
    }

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        LazyColumn(
            state = listState,
            modifier = modifier
                .padding(horizontal = WrapperPaddingHorizontal16)
                .weight(1f)
        ) {
            items(vacancyList) {
                VacancyItem(vacancy = it, onClick = onClick)
            }
            if (isLoadingNextPage) {
                item {
                    LoadingRow()
                }
            }
        }
    }
}
