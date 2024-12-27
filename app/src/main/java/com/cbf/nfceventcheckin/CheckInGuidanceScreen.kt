package com.cbf.nfceventcheckin

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.style.TextAlign
import coil.compose.rememberAsyncImagePainter
import coil.ImageLoader
import coil.request.ImageRequest
import android.os.Build.VERSION.SDK_INT
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@Composable
fun CheckInGuidanceScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        GifImage(
            modifier = Modifier
                .size(250.dp)
        )
        Spacer(modifier = Modifier.size(32.dp))
        Text(
            text = "Hold your phone near the reader to check-in",
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.SemiBold,
                fontSize = 18.sp
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onBackground,
            letterSpacing = 0.5.sp
        )
    }
}

@Composable
fun GifImage(
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val imageLoader = ImageLoader.Builder(context)
        .components {
            if (SDK_INT >= 28) {
                add(ImageDecoderDecoder.Factory())
            } else {
                add(GifDecoder.Factory())
            }
        }
        .build()

    Image(
        painter = rememberAsyncImagePainter(
            ImageRequest.Builder(context)
                .data(data = R.drawable.scan_prompt)
                .build(),
            imageLoader = imageLoader
        ),
        contentDescription = "Scanning animation",
        modifier = modifier,
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewCheckInGuidanceScreen() {
    CheckInGuidanceScreen()
}


