package com.cbf.nfceventcheckin

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun EventDetailsScreen() {
    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_events_image),
                    contentDescription = "Event Image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .height(200.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Coding black Females Meetup",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "12:30 PM")
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Join us for an exciting event where you'll learn and have fun.")
                Text(text = "Agenda:", style = MaterialTheme.typography.titleSmall)
                Spacer(modifier = Modifier.height(8.dp))
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(text = "• Opening Remarks")
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = "• Keynote Speech")
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = "• Networking Session")
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = "• Closing Remarks")
                }
            }
            Button(
                onClick = {
                    // todo Handle check-in
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Check In Now")
            }
        }

    }
}