package com.cbf.nfceventcheckin.screens

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.cbf.nfceventcheckin.R
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
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(imageVector = Icons.Filled.AccountCircle,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(100.dp)
            )
            Spacer(modifier = Modifier.height(20.dp))
            SignInWithEmail(navController, isLoggedInState)
            HorizontalDividerWithInlineText("Or")
            SignInWithGoogle(onClick = { launchGoogleSignIn(launcher, context) })
        }
    }
}

@Composable
fun SignInWithEmail(navController: NavHostController, isLoggedInState: MutableState<Boolean>) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var isValid by remember { mutableStateOf(true) }
    var showErrorMessages by remember { mutableStateOf(false) }

    val sharedPreferences = LocalContext.current.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
    val editor = sharedPreferences.edit()

    OutlinedTextField(
        value = email,
        onValueChange = { email = it },
        label = { Text("Email") },
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
        isError = !isValid && email.isEmpty(),
        supportingText = {
            if (!isValid && email.isEmpty()) {
                Text(text = "Email is required", color = MaterialTheme.colorScheme.error)
            }
        }
    )

    Spacer(modifier = Modifier.height(16.dp))

    OutlinedTextField(
        value = password,
        onValueChange = { password = it },
        label = { Text("Password") },
        modifier = Modifier.fillMaxWidth(),
        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        isError = !isValid && password.isEmpty(),
        supportingText = {
            if (!isValid && password.isEmpty()) {
                Text(text = "Password is required", color = MaterialTheme.colorScheme.error)
            }
        },
        trailingIcon = {
            val icon = if (passwordVisible)
                painterResource(R.drawable.baseline_visibility_off_24) else painterResource(R.drawable.baseline_visibility_24)

            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                Icon(
                    icon,
                    contentDescription = if (passwordVisible) "Hide password" else "Show password"
                )
            }
        }
    )

    Spacer(modifier = Modifier.height(16.dp))

    Button(
        onClick = {
            if (email.isEmpty() || password.isEmpty()) {
                isValid = false
                showErrorMessages = true
            } else {
                isValid = true
                showErrorMessages = false

                editor.putBoolean("is_logged_in", true)
                editor.putString("email", email)
                editor.apply()
                isLoggedInState.value = true

                navController.navigate("event_list_screen")
            }
        },
        modifier = Modifier
            .height(48.dp)
            .border(width = 1.dp, color = Color.Gray, shape = RoundedCornerShape(8.dp)),
        shape = RoundedCornerShape(8.dp)
    ) {
        Text(text = "Sign in with email", fontSize = 16.sp, fontWeight = FontWeight.Medium)
    }
}

@Composable
fun SignInWithGoogle(onClick: () -> Unit) {
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

fun launchSignOut(
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

