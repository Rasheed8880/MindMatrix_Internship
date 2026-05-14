package com.mindmatrix.jalsanchaytracker.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PersonAdd
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
fun RegisterScreen(
    onAuthenticated: () -> Unit,
    onLoginClick: () -> Unit,
    viewModel: AuthViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val loggedIn by viewModel.loggedIn.collectAsState()

    LaunchedEffect(Unit) { viewModel.clearForm() }
    LaunchedEffect(loggedIn) {
        if (loggedIn) onAuthenticated()
    }

    JalBackground {
        LazyColumn(
            Modifier.fillMaxSize().padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item { JalLogoHeader("Register", "Create your rainwater harvesting profile") }
            item {
                PremiumCard {
                    OutlinedTextField(
                        value = state.name,
                        onValueChange = viewModel::updateName,
                        label = { Text("Full name") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = state.email,
                        onValueChange = viewModel::updateEmail,
                        label = { Text("Email") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = state.phone,
                        onValueChange = viewModel::updatePhone,
                        label = { Text("Phone number") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = state.city,
                        onValueChange = viewModel::updateCity,
                        label = { Text("City") },
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
                    OutlinedTextField(
                        value = state.confirmPassword,
                        onValueChange = viewModel::updateConfirmPassword,
                        label = { Text("Confirm password") },
                        visualTransformation = PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Text(
                        "Password must have 8+ characters, one capital letter, one small letter, one number, and one special character.",
                        style = MaterialTheme.typography.bodySmall
                    )
                    state.error?.let { Text(it, color = MaterialTheme.colorScheme.error) }
                    Button(onClick = viewModel::register, modifier = Modifier.fillMaxWidth(), enabled = !state.isLoading) {
                        Icon(Icons.Default.PersonAdd, contentDescription = null)
                        Text("Create account")
                    }
                    OutlinedButton(onClick = onLoginClick, modifier = Modifier.fillMaxWidth(), enabled = !state.isLoading) {
                        Text("Already have an account? Login")
                    }
                    if (state.isLoading) CircularProgressIndicator()
                }
            }
        }
    }
}
