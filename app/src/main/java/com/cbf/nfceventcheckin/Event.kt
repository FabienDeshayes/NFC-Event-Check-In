package com.cbf.nfceventcheckin

data class Event(
    val title: String,
    val time: String,
    val description: String,
    val agenda: List<String>,
    val location: String,
    val contactEmail: String,
    val contactPhone: String,
    val imageResource: Int
)
