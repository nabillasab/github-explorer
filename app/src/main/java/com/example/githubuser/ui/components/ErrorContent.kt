package com.example.githubuser.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.githubuser.R
import com.example.githubuser.ui.theme.GithubUserTheme

@Composable
fun ErrorLoadScreen(errorMsg: String, onRetry: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            ImageDrawable(R.drawable.github_icon, "github icon", size = 100.dp)
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = errorMsg,
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 32.dp),
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = onRetry) {
                LabelMediumText("Retry", color = Color.White)
            }
        }
    }
}

@Composable
fun ErrorFooter(
    errorMsg: String,
    onRetry: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LabelMicroText(errorMsg, color = Color.Red)
        Button(onClick = onRetry) {
            LabelMediumText("Retry", color = Color.White)
        }
    }
}

@Preview
@Composable
fun ErrorPreview() {
    GithubUserTheme {
        ErrorLoadScreen(
            "No Internet Connection No Internet Connection No Internet Connection No Internet Connection",
            onRetry = { }
        )
//            ErrorFooter("No Internet Connection", onRetry = {})
    }
}
