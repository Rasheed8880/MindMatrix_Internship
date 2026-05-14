package com.aipostermaker

import android.os.Bundle
import android.util.Patterns
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.aipostermaker.data.local.PosterDatabase
import com.aipostermaker.data.remote.GeminiApiService
import com.aipostermaker.data.remote.GeminiRemoteDataSource
import com.aipostermaker.data.repository.AiRepositoryImpl
import com.aipostermaker.data.repository.PosterRepositoryImpl
import com.aipostermaker.domain.usecase.DeletePosterUseCase
import com.aipostermaker.domain.usecase.GenerateAiTextUseCase
import com.aipostermaker.domain.usecase.GetAllPostersUseCase
import com.aipostermaker.domain.usecase.SavePosterUseCase
import com.aipostermaker.presentation.screen.PosterEditorScreen
import com.aipostermaker.presentation.viewmodel.PosterEditorViewModel
import com.aipostermaker.presentation.viewmodel.PosterListViewModel
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.ktx.Firebase
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initializeJalFirebase()

        val db =
            Room.databaseBuilder(
                applicationContext,
                PosterDatabase::class.java,
                "poster_db",
            ).build()

        val retrofit =
            Retrofit.Builder()
                .baseUrl("https://generativelanguage.googleapis.com/v1beta/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        val geminiApi = retrofit.create(GeminiApiService::class.java)
        val geminiRemote =
            GeminiRemoteDataSource(
                api = geminiApi,
                apiKey = BuildConfig.GEMINI_API_KEY,
                defaultModel = "gemini-2.5-flash",
            )

        val posterRepository = PosterRepositoryImpl(dao = db.posterDao())
        val aiRepository = AiRepositoryImpl(remote = geminiRemote)

        val savePosterUseCase = SavePosterUseCase(posterRepository)
        val getAllPostersUseCase = GetAllPostersUseCase(posterRepository)
        val deletePosterUseCase = DeletePosterUseCase(posterRepository)
        val generateAiTextUseCase = GenerateAiTextUseCase(aiRepository)

        setContent {
            val editorVm =
                androidx.lifecycle.viewmodel.compose.viewModel<PosterEditorViewModel>(
                    factory =
                        remember {
                            PosterEditorViewModelFactory(
                                savePosterUseCase = savePosterUseCase,
                                generateAiTextUseCase = generateAiTextUseCase,
                            )
                        },
                )

            val listVm =
                androidx.lifecycle.viewmodel.compose.viewModel<PosterListViewModel>(
                    factory =
                        remember {
                            PosterListViewModelFactory(
                                getAllPostersUseCase = getAllPostersUseCase,
                                deletePosterUseCase = deletePosterUseCase,
                            )
                        },
                )

            MaterialTheme(
                colorScheme =
                    lightColorScheme(
                        primary = Color(0xFFC62828),
                        onPrimary = Color.White,
                        primaryContainer = Color(0xFFFFDAD6),
                        onPrimaryContainer = Color(0xFF410003),
                        secondary = Color(0xFF1B5E20),
                        secondaryContainer = Color(0xFFD7F0D1),
                        tertiary = Color(0xFFF7C948),
                        surface = Color(0xFFFFFBF6),
                        surfaceVariant = Color(0xFFF3E7DD),
                        background = Color(0xFFFFFBF6),
                    ),
            ) {
                Surface(color = MaterialTheme.colorScheme.background) {
                    var isSignedIn by remember { mutableStateOf(Firebase.auth.currentUser != null) }
                    DisposableEffect(Unit) {
                        val listener = FirebaseAuth.AuthStateListener { auth ->
                            isSignedIn = auth.currentUser != null
                        }
                        Firebase.auth.addAuthStateListener(listener)
                        onDispose { Firebase.auth.removeAuthStateListener(listener) }
                    }
                    if (isSignedIn) {
                        PosterEditorScreen(
                            editorViewModel = editorVm,
                            listViewModel = listVm,
                        )
                    } else {
                        AuthScreen(onContinue = { isSignedIn = true })
                    }
                }
            }
        }
    }

    private fun initializeJalFirebase() {
        if (FirebaseApp.getApps(this).isNotEmpty()) return

        val options =
            FirebaseOptions.Builder()
                .setProjectId("jal-sanchay-tracker-2b86f")
                .setApplicationId("1:170727521791:android:2ba839c06271aa8fdc3bca")
                .setApiKey("AIzaSyBx0pVUuDhBUssyd2kppf9GxqnoYlYoQnY")
                .setStorageBucket("jal-sanchay-tracker-2b86f.firebasestorage.app")
                .build()

        FirebaseApp.initializeApp(this, options)
    }
}

@Composable
private fun AuthScreen(onContinue: () -> Unit) {
    var isRegister by remember { mutableStateOf(false) }
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    val auth = remember { Firebase.auth }
    val quote =
        if (isRegister) {
            "Create with confidence. Your next poster starts with one clear idea."
        } else {
            "Design is simple when the workspace feels calm."
        }

    Box(
        Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(Color(0xFFFFF7E6), Color(0xFFEFF7EF))))
            .padding(20.dp),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(22.dp))
                .background(MaterialTheme.colorScheme.surface)
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text("NAMMA POSTER APP", style = MaterialTheme.typography.headlineSmall, color = MaterialTheme.colorScheme.primary)
            Text(quote, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            if (isRegister) {
                OutlinedTextField(
                    value = name,
                    onValueChange = {
                        name = it
                        errorMessage = null
                    },
                    label = { Text("Name") },
                    singleLine = true,
                    isError = errorMessage?.contains("name", ignoreCase = true) == true,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
            OutlinedTextField(
                value = email,
                onValueChange = {
                    email = it
                    errorMessage = null
                },
                label = { Text("Email") },
                singleLine = true,
                keyboardOptions =
                    KeyboardOptions(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Next,
                    ),
                isError = errorMessage?.contains("email", ignoreCase = true) == true,
                modifier = Modifier.fillMaxWidth(),
            )
            OutlinedTextField(
                value = password,
                onValueChange = {
                    password = it
                    errorMessage = null
                },
                label = { Text("Password") },
                singleLine = true,
                keyboardOptions =
                    KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done,
                    ),
                visualTransformation = PasswordVisualTransformation(),
                isError = errorMessage?.contains("password", ignoreCase = true) == true,
                modifier = Modifier.fillMaxWidth(),
            )
            errorMessage?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
            Button(
                onClick = {
                    val validationError = validateAuthForm(
                        isRegister = isRegister,
                        name = name,
                        email = email,
                        password = password,
                    )
                    if (validationError != null) {
                        errorMessage = validationError
                        return@Button
                    }

                    isLoading = true
                    errorMessage = null
                    val trimmedEmail = email.trim()
                    if (isRegister) {
                        auth.createUserWithEmailAndPassword(trimmedEmail, password)
                            .addOnCompleteListener { task ->
                                isLoading = false
                                if (task.isSuccessful) {
                                    val displayName = name.trim()
                                    if (displayName.isNotEmpty()) {
                                        auth.currentUser?.updateProfile(
                                            userProfileChangeRequest {
                                                this.displayName = displayName
                                            },
                                        )
                                    }
                                    onContinue()
                                } else {
                                    errorMessage = task.exception?.localizedMessage ?: "Registration failed. Please try again."
                                }
                            }
                    } else {
                        auth.signInWithEmailAndPassword(trimmedEmail, password)
                            .addOnCompleteListener { task ->
                                isLoading = false
                                if (task.isSuccessful) {
                                    onContinue()
                                } else {
                                    errorMessage = task.exception?.localizedMessage ?: "Login failed. Please check your email and password."
                                }
                            }
                    }
                },
                enabled = !isLoading,
                modifier = Modifier.fillMaxWidth(),
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(18.dp),
                        color = MaterialTheme.colorScheme.onPrimary,
                        strokeWidth = 2.dp,
                    )
                } else {
                    Text(if (isRegister) "Create Account" else "Login")
                }
            }
            TextButton(
                onClick = {
                    isRegister = !isRegister
                    errorMessage = null
                },
                enabled = !isLoading,
            ) {
                Text(if (isRegister) "Already have an account? Login" else "New user? Register")
            }
        }
    }
}

private fun validateAuthForm(
    isRegister: Boolean,
    name: String,
    email: String,
    password: String,
): String? {
    val trimmedEmail = email.trim()
    val trimmedName = name.trim()

    return when {
        isRegister && trimmedName.length < 2 -> "Enter a valid name."
        trimmedEmail.isBlank() -> "Enter your email address."
        !Patterns.EMAIL_ADDRESS.matcher(trimmedEmail).matches() -> "Enter a valid email address."
        password.isBlank() -> "Enter your password."
        password.length < 6 -> "Password must be at least 6 characters."
        else -> null
    }
}

private class PosterEditorViewModelFactory(
    private val savePosterUseCase: SavePosterUseCase,
    private val generateAiTextUseCase: GenerateAiTextUseCase,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PosterEditorViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PosterEditorViewModel(
                savePosterUseCase = savePosterUseCase,
                generateAiTextUseCase = generateAiTextUseCase,
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}

private class PosterListViewModelFactory(
    private val getAllPostersUseCase: GetAllPostersUseCase,
    private val deletePosterUseCase: DeletePosterUseCase,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PosterListViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PosterListViewModel(
                getAllPostersUseCase = getAllPostersUseCase,
                deletePosterUseCase = deletePosterUseCase,
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}

