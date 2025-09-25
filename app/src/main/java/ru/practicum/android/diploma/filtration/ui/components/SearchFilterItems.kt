package ru.practicum.android.diploma.filtration.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.core.ui.components.SearchBar
import ru.practicum.android.diploma.core.ui.theme.blackUniversal

@Composable
fun SearchFilterItems(
    text: String,
    placeholder: String,
    modifier: Modifier = Modifier,
    onSearchHandler: (String) -> Unit = {},
    onResetRequest: () -> Unit = {}
) {
    SearchBar(
        modifier = modifier,
        value = text,
        onValueChange = onSearchHandler,
        placeholder = {
            Text(
                color = MaterialTheme.colorScheme.onTertiary,
                text = placeholder,
                style = MaterialTheme.typography.bodyLarge,
            )
        },
        trailingIcon = {
            val ico = if (text.isEmpty()) R.drawable.ic_search else R.drawable.ic_close
            Icon(
                tint = blackUniversal,
                modifier = Modifier.clickable {
                    onResetRequest()
                },
                imageVector = ImageVector.vectorResource(id = ico),
                contentDescription = null
            )
        }
    )
}
