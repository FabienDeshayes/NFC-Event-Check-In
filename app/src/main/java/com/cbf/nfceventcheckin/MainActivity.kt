package com.cbf.nfceventcheckin

import android.content.Context
import android.content.SharedPreferences
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.cbf.nfceventcheckin.ui.theme.NFCEventCheckInTheme

class MainActivity : ComponentActivity() {
    private var nfcAdapter: NfcAdapter? = null
    private lateinit var sharedPreferences: SharedPreferences
    private var isLoggedIn: Boolean = false
    private var isCheckedIn: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedPreferences = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        isLoggedIn = sharedPreferences.getBoolean("is_logged_in", false)

        setContent {
            NFCEventCheckInTheme {
                Navigation(isLoggedIn = isLoggedIn, isCheckedIn = isCheckedIn)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        enableNfcForegroundDispatch()
    }

    override fun onPause() {
        super.onPause()
        disableNfcForegroundDispatch()
    }

    private fun handleNfcTag(tag: Tag) {
        val sharedPreferences = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        val loggedInEmail = sharedPreferences.getString("email", "No email found")

        Log.d(
            "Main",
            "Tag Serial number: ${tag.id.joinToString(":") { String.format("%02X", it) }}"
        )
        Log.d("Main",
            "Logged in as: $loggedInEmail")
        isCheckedIn = true
    }

    private fun enableNfcForegroundDispatch() {
        if (NfcAdapter.ACTION_NDEF_DISCOVERED == intent.action) {
            val tag: Tag? = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG)
            Log.d("Main", "NFC Tag detected")
            tag?.let {
                Log.d("Main", "NFC Tag discovered and handled.")
                handleNfcTag(it)
            }
        }
    }

    private fun disableNfcForegroundDispatch() {
        nfcAdapter?.disableForegroundDispatch(this)
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    val checkedInUsers = mutableListOf("John Doe", "Jane Smith", "Alice Johnson")
    NFCEventCheckInTheme {
        AdminScreen(checkedInUsers)
    }
}