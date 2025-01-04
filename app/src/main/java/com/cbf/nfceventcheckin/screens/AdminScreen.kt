package com.cbf.nfceventcheckin.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.cbf.nfceventcheckin.DatabaseHelper
import com.cbf.nfceventcheckin.Event

@Composable
fun AdminScreen(event: Event) {
    val context = LocalContext.current
    val dbHelper = DatabaseHelper(context)
    val checkedInEmails = remember { mutableStateListOf<String>() }

    LaunchedEffect(Unit) {
        checkedInEmails.clear()
        checkedInEmails.addAll(dbHelper.getAllCheckedInEmails(event.tagSerialNumber))
    }

    Surface(
        modifier = Modifier
            .fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            EventHeader(event)
            Spacer(modifier = Modifier.height(8.dp))
            EventTime(event)
            EventLocation(event)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = if (checkedInEmails.size > 0) "${checkedInEmails.size} people have checked in" else "No one has checked in yet.",
                style = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onSurfaceVariant),
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            if (checkedInEmails.isNotEmpty()) {
                LazyColumn(modifier = Modifier.fillMaxWidth()) {
                    items(checkedInEmails) { email ->
                        CheckedInUserItem(email = email, checkInTime = "12:21 PM")
                    }
                }
            }
        }
    }
}

@Composable
fun CheckedInUserItem(email: String, checkInTime: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        HorizontalDivider(
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f),
            modifier = Modifier.padding(8.dp)
        )

        Text(
            text = email,
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "Checked in at: $checkInTime",
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}