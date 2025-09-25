package ru.practicum.android.diploma.filtration.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.core.ui.theme.ApplicationTheme
import ru.practicum.android.diploma.core.ui.theme.WidthForInfoImage328

@Composable
fun ShowErrorRegion() {
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            modifier = Modifier.width(WidthForInfoImage328),
            contentScale = ContentScale.Crop,
            painter = painterResource(R.drawable.region_error),
            contentDescription = null
        )
        Text(
            text = stringResource(R.string.work_place_error),
            style = MaterialTheme.typography.titleLarge,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
    }
}

@Preview
@Composable
fun ShowErrorRegionPreview() {
    ApplicationTheme {
        ShowErrorRegion()
    }
}

@Composable
fun ShowEmptyRegion() {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            modifier = Modifier.width(WidthForInfoImage328),
            contentScale = ContentScale.Crop,
            painter = painterResource(R.drawable.no_region),
            contentDescription = null
        )
        Text(
            text = stringResource(R.string.no_region),
            style = MaterialTheme.typography.titleLarge
        )
    }
}
