package com.example.githubuser.ui.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp

@Composable
fun ToolbarTitle(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.onBackground
) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleLarge,
        color = color,
        modifier = modifier,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
    )
}

@Composable
fun SectionTitle(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.onBackground
) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleMedium,
        color = color,
        modifier = modifier,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
    )
}

@Composable
fun BodyText(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.onBackground
) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodySmall,
        color = color,
        modifier = modifier,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
    )
}

@Composable
fun LabelLargeText(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.onBackground
) {
    Text(
        text = text,
        style = MaterialTheme.typography.labelLarge,
        color = color,
        modifier = modifier,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
    )
}

@Composable
fun LabelMediumText(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.onBackground
) {
    Text(
        text = text,
        style = MaterialTheme.typography.labelMedium,
        color = color,
        modifier = modifier,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
    )
}

@Composable
fun LabelSmallText(
    text: String,
    modifier: Modifier = Modifier,
    maxLines: Int = 1,
    color: Color = MaterialTheme.colorScheme.onBackground
) {
    Text(
        text = text,
        style = MaterialTheme.typography.labelSmall,
        fontWeight = FontWeight.Normal,
        color = color,
        modifier = modifier,
        maxLines = maxLines,
        overflow = TextOverflow.Ellipsis
    )
}

@Composable
fun LabelMicroText(
    text: String, modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.onBackground
) {
    Text(
        text = text,
        fontSize = 10.sp,
        maxLines = 1,
        color = color,
        overflow = TextOverflow.Ellipsis,
        modifier = modifier
    )
}