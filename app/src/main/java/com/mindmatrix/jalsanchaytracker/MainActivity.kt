package com.mindmatrix.jalsanchaytracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.mindmatrix.jalsanchaytracker.ui.JalSanchayRoot
import com.mindmatrix.jalsanchaytracker.ui.theme.JalSanchayTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            JalSanchayTheme {
                JalSanchayRoot()
            }
        }
    }
}
