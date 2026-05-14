package com.mindmatrix.jalsanchaytracker.viewmodel

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mindmatrix.jalsanchaytracker.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AuthUiState(
    val name: String = "",
    val email: String = "",
    val phone: String = "",
    val city: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {
    val loggedIn: StateFlow<Boolean> = authRepository.observeLoggedIn()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), authRepository.isLoggedIn)

    private val _state = MutableStateFlow(AuthUiState())
    val state = _state.asStateFlow()

    fun updateName(value: String) {
        _state.value = _state.value.copy(name = value, error = null)
    }

    fun updateEmail(value: String) {
        _state.value = _state.value.copy(email = value, error = null)
    }

    fun updatePhone(value: String) {
        _state.value = _state.value.copy(phone = value.filter(Char::isDigit).take(10), error = null)
    }

    fun updateCity(value: String) {
        _state.value = _state.value.copy(city = value, error = null)
    }

    fun updatePassword(value: String) {
        _state.value = _state.value.copy(password = value, error = null)
    }

    fun updateConfirmPassword(value: String) {
        _state.value = _state.value.copy(confirmPassword = value, error = null)
    }

    fun login() = viewModelScope.launch {
        val email = state.value.email.trim()
        val password = state.value.password
        val validationError = validateLogin(email, password)
        if (validationError != null) {
            _state.value = _state.value.copy(error = validationError)
            return@launch
        }
        runAuth { authRepository.login(email, password) }
    }

    fun register() = viewModelScope.launch {
        val current = state.value
        val name = current.name.trim()
        val email = current.email.trim()
        val phone = current.phone.trim()
        val city = current.city.trim()
        val password = current.password
        val confirmPassword = current.confirmPassword
        val validationError = validateRegister(name, email, phone, city, password, confirmPassword)
        if (validationError != null) {
            _state.value = _state.value.copy(error = validationError)
            return@launch
        }
        runAuth {
            authRepository.register(
                name = name,
                email = email,
                phone = phone,
                city = city,
                password = password
            )
        }
    }

    fun clearForm() {
        _state.value = AuthUiState()
    }

    fun logout() = authRepository.logout()

    private suspend fun runAuth(block: suspend () -> Unit) {
        _state.value = _state.value.copy(isLoading = true, error = null)
        try {
            block()
        } catch (error: Exception) {
            _state.value = _state.value.copy(error = error.message ?: "Authentication failed")
        } finally {
            _state.value = _state.value.copy(isLoading = false)
        }
    }

    private fun validateLogin(email: String, password: String): String? {
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) return "Enter a valid email address"
        if (password.isBlank()) return "Enter your password"
        return null
    }

    private fun validateRegister(
        name: String,
        email: String,
        phone: String,
        city: String,
        password: String,
        confirmPassword: String
    ): String? {
        if (name.length < 3) return "Name must be at least 3 characters"
        if (!name.all { it.isLetter() || it.isWhitespace() }) return "Name should contain letters only"
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) return "Enter a valid email address"
        if (phone.length != 10) return "Phone number must be 10 digits"
        if (phone.firstOrNull() !in listOf('6', '7', '8', '9')) return "Enter a valid Indian mobile number"
        if (city.length < 2) return "Enter your city"
        if (!city.all { it.isLetter() || it.isWhitespace() }) return "City should contain letters only"
        if (password.length < 8) return "Password must be at least 8 characters"
        if (!password.any(Char::isUpperCase)) return "Password needs one capital letter"
        if (!password.any(Char::isLowerCase)) return "Password needs one small letter"
        if (!password.any(Char::isDigit)) return "Password needs one number"
        if (!password.any { !it.isLetterOrDigit() }) return "Password needs one special character"
        if (password != confirmPassword) return "Passwords do not match"
        return null
    }
}
