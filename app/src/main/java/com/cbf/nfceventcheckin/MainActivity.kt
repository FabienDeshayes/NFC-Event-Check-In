package com.cbf.nfceventcheckin

import android.content.Context
import android.content.SharedPreferences
import android.nfc.NfcAdapter
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ExitToApp
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
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.cbf.nfceventcheckin.screens.launchSignOut
import com.cbf.nfceventcheckin.ui.theme.NFCEventCheckInTheme
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class MainActivity : ComponentActivity() {
    private var nfcAdapter: NfcAdapter? = null
    private lateinit var sharedPreferences: SharedPreferences
    private var isCheckedIn: Boolean = false
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        nfcAdapter = NfcAdapter.getDefaultAdapter(this)

        sharedPreferences = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        auth = Firebase.auth

        val isLoggedInState = mutableStateOf(sharedPreferences.getBoolean("is_logged_in", false))

        setContent {
            val navController = rememberNavController()

            NFCEventCheckInTheme {
                Scaffold(
                    topBar = {
                        if (isLoggedInState.value) {
                            TopAppBar(navController, isLoggedInState)
                        }
                    },
                    content = { padding ->
                        Surface(
                            modifier = Modifier.padding(padding),
                            color = MaterialTheme.colorScheme.background,
                        ) {
                            Navigation(
                                isLoggedIn = isLoggedInState.value,
                                isCheckedIn = isCheckedIn,
                                navController,
                                isLoggedInState
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
        navController: NavController,
        isLoggedInState: MutableState<Boolean>
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
            },
            actions = {
                IconButton(onClick = { launchSignOut(applicationContext, auth, sharedPreferences, isLoggedInState, navController) }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                        contentDescription = "Sign Out",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        )
    }

    private fun handleNfcTag(byteArray: ByteArray) {
        val sharedPreferences = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        val loggedInEmail = sharedPreferences.getString("email", "")
        val dateFormat = SimpleDateFormat("hh:mma", Locale.getDefault())
        val timestamp = dateFormat.format(Calendar.getInstance().time)

        val serialNumber = byteArray.joinToString(":") { String.format("%02X", it) }

        Log.d("Main", "Tag Serial number: $serialNumber")
        Log.d("Main", "Logged in as: $loggedInEmail")

        val dbHelper = DatabaseHelper(this)
        if (loggedInEmail != "") {
            dbHelper.insertNfcTag(serialNumber, loggedInEmail!!, timestamp)
            isCheckedIn = true
        }
    }

    private fun enableNfcForegroundDispatch() {
        if (nfcAdapter != null && nfcAdapter?.isEnabled == true) {
            if (NfcAdapter.ACTION_NDEF_DISCOVERED == intent.action) {
                val byteArray: ByteArray? = intent.getByteArrayExtra(NfcAdapter.EXTRA_ID)
                Log.d("Main", "NFC Tag detected")
                byteArray?.let {
                    Log.d("Main", "NFC Tag discovered and handled.")
                    handleNfcTag(it) // Process the NFC tag data
                }
            }
        } else {
            Log.e("Main", "NFC is not supported or enabled on this device.")
        }
    }

    private fun disableNfcForegroundDispatch() {
        nfcAdapter?.disableForegroundDispatch(this)
    }
}