package com.cbf.nfceventcheckin

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

@Composable
fun LoginScreen(navController: NavHostController, isLoggedInState: MutableState<Boolean>) {
    val context = LocalContext.current
    val auth = FirebaseAuth.getInstance()
    val sharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

    val launcher = rememberGoogleSignInLauncher(
        auth = auth,
        sharedPreferences = sharedPreferences,
        navController = navController,
        context = context,
        isLoggedInState = isLoggedInState
    )

    Surface(modifier = Modifier.fillMaxSize()) {
        LoginScreenContent(
            onGoogleSignIn = { launchGoogleSignIn(launcher, context) }
        )
    }
}

@Composable
fun LoginScreenContent(onGoogleSignIn: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Filled.AccountCircle,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(72.dp)
        )
        Spacer(modifier = Modifier.height(20.dp))
        GoogleSignInButton(onClick = onGoogleSignIn)
    }
}

@Composable
fun rememberGoogleSignInLauncher(
    auth: FirebaseAuth,
    sharedPreferences: SharedPreferences,
    navController: NavHostController,
    context: Context,
    isLoggedInState: MutableState<Boolean>
): ActivityResultLauncher<Intent> {
    val editor = sharedPreferences.edit()
    return rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        handleGoogleSignInResult(
            result = result,
            auth = auth,
            editor = editor,
            navController = navController,
            context = context,
            isLoggedInState = isLoggedInState
        )
    }
}

@Composable
fun GoogleSignInButton(onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .height(48.dp)
            .border(
                width = 1.dp,
                color = Color.Gray,
                shape = RoundedCornerShape(8.dp)
            ),
        colors = ButtonDefaults.buttonColors(Color.White),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            Image(
                painter = painterResource(id = R.drawable.google_logo),
                contentDescription = "Google Logo",
                modifier = Modifier
                    .size(24.dp)
                    .padding(end = 8.dp)
            )
            Text(
                text = "Sign in with Google",
                color = Color.Black,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

fun launchGoogleSignIn(
    launcher: ActivityResultLauncher<Intent>,
    context: Context
) {
    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken(context.getString(R.string.public_server_client_id))
        .requestEmail()
        .build()
    val googleSignInClient = GoogleSignIn.getClient(context, gso)
    launcher.launch(googleSignInClient.signInIntent)
}

fun handleGoogleSignInResult(
    result: ActivityResult,
    auth: FirebaseAuth,
    editor: SharedPreferences.Editor,
    navController: NavHostController,
    context: Context,
    isLoggedInState: MutableState<Boolean>
) {
    try {
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        val account = task.getResult(ApiException::class.java)
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)

        auth.signInWithCredential(credential)
            .addOnCompleteListener { authTask ->
                if (authTask.isSuccessful) {
                    editor.putBoolean("is_logged_in", true)
                    editor.putString("email", account.email)
                    editor.apply()
                    isLoggedInState.value = true
                    navController.navigate("event_list_screen")
                } else {
                    Toast.makeText(context, "Authentication failed", Toast.LENGTH_SHORT).show()
                }
            }
    } catch (e: ApiException) {
        e.printStackTrace()
        Toast.makeText(context, "Google Sign-In failed", Toast.LENGTH_SHORT).show()
    }
}

fun onSignOut(
    context: Context,
    auth: FirebaseAuth,
    sharedPreferences: SharedPreferences,
    isLoggedInState: MutableState<Boolean>,
    navController: NavController
) {
    auth.signOut()

    isLoggedInState.value = false
    sharedPreferences.edit().apply {
        remove("is_logged_in")
        remove("email")
        apply()
    }

    navController.navigate("login_screen")
    Toast.makeText(context, "Signed out successfully", Toast.LENGTH_SHORT).show()
}

