package com.cbf.nfceventcheckin

import android.net.Uri
import android.view.SurfaceView
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import coil3.compose.AsyncImage
import coil3.request.ImageRequest

@Composable
fun CheckInGuidanceScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CheckInGuidanceGifImage(
            modifier = Modifier
                .size(380.dp)
                .padding(16.dp)
                .clip(CircleShape)
                .border(4.dp, MaterialTheme.colorScheme.primary, CircleShape)
        )
        Spacer(modifier = Modifier.size(32.dp))
        Text(
            text = "Hold your Phone to the Event Reader to Check In",
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp),
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            textAlign = TextAlign.Center,
            letterSpacing = 0.5.sp
        )
    }
}

@Composable
fun CheckInGuidanceGifImage(
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current

    AsyncImage(
        model = ImageRequest.Builder(context)
            .data(R.drawable.nfc_prompt)
            .build(),
        contentDescription = "Scanning animation",
        modifier = modifier,
    )
}


@Composable
fun CheckInGuidanceVideoPlayer(
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val videoUri: Uri = Uri.parse("android.resource://${context.packageName}/raw/nfc_prompt")

    val exoPlayer = remember(context) {
        ExoPlayer.Builder(context).build().apply {
            val mediaItem = MediaItem.fromUri(videoUri)
            setMediaItem(mediaItem)
        }
    }

    LaunchedEffect(exoPlayer) {
        exoPlayer.prepare()
        exoPlayer.play()
    }
    DisposableEffect(exoPlayer) {
        onDispose {
            exoPlayer.release()
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        AndroidView(factory = {
            SurfaceView(context).apply {
                exoPlayer.setVideoSurfaceHolder(holder)
            }
        })
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewCheckInGuidanceScreen() {
    CheckInGuidanceScreen()
}


