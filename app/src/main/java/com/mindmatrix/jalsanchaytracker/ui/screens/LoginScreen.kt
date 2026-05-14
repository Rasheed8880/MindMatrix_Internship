package com.mindmatrix.jalsanchaytracker.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Login
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mindmatrix.jalsanchaytracker.ui.components.JalBackground
import com.mindmatrix.jalsanchaytracker.ui.components.JalLogoHeader
import com.mindmatrix.jalsanchaytracker.ui.components.PremiumCard
import com.mindmatrix.jalsanchaytracker.viewmodel.AuthViewModel

@Composable
fun LoginScreen(
    onAuthenticated: () -> Unit,
    onRegisterClick: () -> Unit,
    viewModel: AuthViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val loggedIn by viewModel.loggedIn.collectAsState()

    LaunchedEffect(Unit) { viewModel.clearForm() }
    LaunchedEffect(loggedIn) {
        if (loggedIn) onAuthenticated()
    }

    JalBackground {
        Column(
            Modifier.fillMaxSize().padding(24.dp),
            verticalArrangement = Arrangement.Center
        ) {
            JalLogoHeader("Login", "Welcome back to Jal-Sanchay Tracker")
            PremiumCard(Modifier.padding(top = 24.dp)) {
                OutlinedTextField(
                    value = state.email,
                    onValueChange = viewModel::updateEmail,
                    label = { Text("Email") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = state.password,
                    onValueChange = viewModel::updatePassword,
                    label = { Text("Password") },
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                state.error?.let { Text(it, color = MaterialTheme.colorScheme.error) }
                Button(onClick = viewModel::login, modifier = Modifier.fillMaxWidth(), enabled = !state.isLoading) {
                    Icon(Icons.Default.Login, contentDescription = null)
                    Text("Login")
                }
                OutlinedButton(onClick = onRegisterClick, modifier = Modifier.fillMaxWidth(), enabled = !state.isLoading) {
                    Text("Create new account")
                }
                if (state.isLoading) CircularProgressIndicator()
            }
        }
    }
}
