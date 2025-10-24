package com.example.githubuser.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.githubuser.ui.theme.DarkBlue
import com.example.githubuser.ui.theme.GithubUserTheme
import com.example.githubuser.ui.theme.White

@Composable
fun InfoTooltip(text: String, modifier: Modifier = Modifier) {
    val backgroundColor = Color(0xFF49454F)
    val contentColor = Color.White

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .background(backgroundColor, shape = RoundedCornerShape(4.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Icon(
            imageVector = Icons.Default.Info,
            contentDescription = "info",
            tint = contentColor,
            modifier = Modifier.size(16.dp)
        )
        LabelSmallText(
            text = text,
            color = contentColor,
            modifier = Modifier.padding(start = 4.dp)
        )
    }
}

@Composable
fun TextTooltip(text: String, modifier: Modifier = Modifier) {
    val backgroundColor = DarkBlue
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .border(width = 1.dp, color = backgroundColor, shape = RoundedCornerShape(4.dp))
            .padding(horizontal = 8.dp, vertical = 2.dp)

    ) {
        LabelSmallText(
            text = text,
            color = backgroundColor
        )
    }
}

@Preview(showBackground = true)
@Composable
fun GithubUserListPreview() {
    GithubUserTheme {
        Column {
            TextTooltip("Public")
            Spacer(modifier = Modifier.size(16.dp))
            InfoTooltip("Only non-forked repositories are displayed")
        }
    }
}
