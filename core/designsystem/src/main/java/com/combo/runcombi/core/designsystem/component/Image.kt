package com.combo.runcombi.core.designsystem.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import coil.compose.AsyncImage
import coil.request.ImageRequest
import androidx.compose.ui.platform.LocalContext

@Composable
fun StableImage(
    @DrawableRes drawableResId: Int,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Fit,
    description: String? = null,
) {
    val painter = painterResource(id = drawableResId)
    Image(
        modifier = modifier,
        painter = painter,
        contentScale = contentScale,
        contentDescription = description
    )
}

@Composable
fun NetworkImage(
    imageUrl: String?,
    @DrawableRes drawableResId: Int,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Fit,
    description: String? = null,
) {
    val context = LocalContext.current
    AsyncImage(
        model = imageUrl?.let {
            ImageRequest.Builder(context)
                .data(it)
                .crossfade(true)
                .build()
        } ?: drawableResId,
        contentDescription = description,
        modifier = modifier,
        contentScale = contentScale,
        error = painterResource(id = drawableResId),
        placeholder = painterResource(id = drawableResId)
    )
}
