package com.mindmatrix.jalsanchaytracker.ui

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.mindmatrix.jalsanchaytracker.ui.screens.AiScreen
import com.mindmatrix.jalsanchaytracker.ui.screens.CalculatorScreen
import com.mindmatrix.jalsanchaytracker.ui.screens.CommunityScreen
import com.mindmatrix.jalsanchaytracker.ui.screens.DashboardScreen
import com.mindmatrix.jalsanchaytracker.ui.screens.KnowledgeScreen
import com.mindmatrix.jalsanchaytracker.ui.screens.LoginScreen
import com.mindmatrix.jalsanchaytracker.ui.screens.OnboardingScreen
import com.mindmatrix.jalsanchaytracker.ui.screens.RegisterScreen
import com.mindmatrix.jalsanchaytracker.viewmodel.AuthViewModel

private sealed class Route(val value: String, val label: String) {
    data object Onboarding : Route("onboarding", "Intro")
    data object Login : Route("login", "Login")
    data object Register : Route("register", "Register")
    data object Dashboard : Route("dashboard", "Home")
    data object Calculator : Route("calculator", "Track")
    data object Ai : Route("ai", "Jal AI")
    data object Community : Route("community", "Jal Circle")
    data object Knowledge : Route("knowledge", "Learn")
}

@Composable
fun JalSanchayRoot(authViewModel: AuthViewModel = hiltViewModel()) {
    val navController = rememberNavController()
    val loggedIn by authViewModel.loggedIn.collectAsState()
    val start = if (loggedIn) Route.Dashboard.value else Route.Onboarding.value
    val bottomRoutes = listOf(Route.Dashboard, Route.Calculator, Route.Ai, Route.Community, Route.Knowledge)
    val backStack by navController.currentBackStackEntryAsState()
    val currentRoute = backStack?.destination?.route

    Scaffold(
        bottomBar = {
            if (currentRoute in bottomRoutes.map { it.value }) {
                NavigationBar {
                    bottomRoutes.forEach { route ->
                        val selected = backStack?.destination?.hierarchy?.any { it.route == route.value } == true
                        NavigationBarItem(
                            selected = selected,
                            onClick = {
                                navController.navigate(route.value) {
                                    popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = { Icon(route.icon(), contentDescription = route.label) },
                            label = { Text(route.label) }
                        )
                    }
                }
            }
        }
    ) { padding ->
        Crossfade(targetState = start, label = "root") { startDestination ->
            NavHost(
                navController = navController,
                startDestination = startDestination,
                modifier = Modifier.padding(padding)
            ) {
                composable(Route.Onboarding.value) {
                    OnboardingScreen(onFinished = { navController.navigate(Route.Login.value) })
                }
                composable(Route.Login.value) {
                    LoginScreen(
                        onAuthenticated = {
                            navController.navigate(Route.Dashboard.value) {
                                popUpTo(Route.Onboarding.value) { inclusive = true }
                            }
                        },
                        onRegisterClick = { navController.navigate(Route.Register.value) }
                    )
                }
                composable(Route.Register.value) {
                    RegisterScreen(
                        onAuthenticated = {
                            navController.navigate(Route.Dashboard.value) {
                                popUpTo(Route.Onboarding.value) { inclusive = true }
                            }
                        },
                        onLoginClick = { navController.navigate(Route.Login.value) }
                    )
                }
                composable(Route.Dashboard.value) {
                    DashboardScreen(onLogout = authViewModel::logout)
                }
                composable(Route.Calculator.value) { CalculatorScreen() }
                composable(Route.Ai.value) { AiScreen() }
                composable(Route.Community.value) { CommunityScreen() }
                composable(Route.Knowledge.value) { KnowledgeScreen() }
            }
        }
    }
}

private fun Route.icon() = when (this) {
    Route.Dashboard -> Icons.Default.Home
    Route.Calculator -> Icons.Default.Calculate
    Route.Ai -> Icons.Default.AutoAwesome
    Route.Community -> Icons.Default.Groups
    Route.Knowledge -> Icons.Default.MenuBook
    else -> Icons.Default.Home
}
