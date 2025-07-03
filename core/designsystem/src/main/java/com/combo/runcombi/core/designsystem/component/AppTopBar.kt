package com.combo.runcombi.core.designsystem.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.combo.runcombi.core.designsystem.R
import com.combo.runcombi.core.designsystem.theme.RunCombiTypography.title3
import com.combo.runcombi.core.designsystem.theme.WhiteFF

@Composable
fun RunCombiAppTopBar(
    title: String = "",
    onBack: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onBack) {
            StableImage(
                drawableResId = R.drawable.ic_back,
                modifier = Modifier
                    .size(24.dp)
            )
        }
        Text(
            text = title,
            color = WhiteFF,
            style = title3,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center,
            modifier = Modifier.weight(1f)
        )
        Spacer(modifier = Modifier.width(24.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewRunCombiTopBar() {
    RunCombiAppTopBar(title = "Title", onBack = {})
}