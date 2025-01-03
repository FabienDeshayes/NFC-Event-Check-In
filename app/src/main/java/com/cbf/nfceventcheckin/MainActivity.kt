package com.cbf.nfceventcheckin

import android.content.Context
import android.content.SharedPreferences
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
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
            val navController = rememberNavController()

            NFCEventCheckInTheme {
                Scaffold(
                    topBar = {
                        if (isLoggedIn) {
                            TopAppBar(navController)
                        }
                    },
                    content = { padding ->
                        Surface(
                            modifier = Modifier.padding(padding),
                            color = MaterialTheme.colorScheme.background,
                        ) {
                            Navigation(
                                isLoggedIn = isLoggedIn,
                                isCheckedIn = isCheckedIn,
                                navController
                            )
                        }
                    }
                )
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

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun TopAppBar(
        navController: NavController
    ) {
        val currentBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = currentBackStackEntry?.destination?.route
        TopAppBar(
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                titleContentColor = MaterialTheme.colorScheme.primary,
            ),
            title = {
                Text("Event Check-In")
            },
            navigationIcon = {
                if (currentRoute != "event_list_screen") {
                    println("seda" + navController.currentDestination?.route)
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        )
    }

    private fun handleNfcTag(tag: Tag) {
        val sharedPreferences = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        val loggedInEmail = sharedPreferences.getString("email", "")

        val serialNumber = tag.id.joinToString(":") { String.format("%02X", it) }

        Log.d("Main", "Tag Serial number: $serialNumber")
        Log.d("Main", "Logged in as: $loggedInEmail")

        val dbHelper = DatabaseHelper(this)
        if (loggedInEmail != "") {
            dbHelper.insertNfcTag(serialNumber, loggedInEmail!!)
            isCheckedIn = true
        }
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