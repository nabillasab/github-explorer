package com.example.githubuser.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.githubuser.R

@Composable
fun UserAvatar(
    avatarUrl: String,
    size: Dp,
    modifier: Modifier = Modifier
) {
    val modifier = modifier
        .size(size)
        .clip(CircleShape)
    LoadImage(avatarUrl, modifier)
}

@Composable
fun LoadImage(url: String, modifier: Modifier) {
    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(url)
            .placeholder(R.drawable.github_icon)
            .error(R.drawable.img_error)
            .crossfade(true)
            .build(),
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = modifier
    )
}

@Composable
fun ImageDrawable(drawable: Int, contentDesc: String, size: Dp = 12.dp) {
    Icon(
        painter = painterResource(drawable),
        contentDescription = contentDesc,
        modifier = Modifier
            .size(size)
    )
}

@Composable
fun ImageIcon(imageVector: ImageVector, contentDesc: String, size: Dp = 12.dp) {
    Icon(
        imageVector = imageVector,
        contentDescription = contentDesc,
        modifier = Modifier
            .size(size)
    )
}

@Composable
fun SmallDot(color: Color = Color.Black, size: Dp = 8.dp, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .size(size)
            .background(color, shape = CircleShape)
    )
}