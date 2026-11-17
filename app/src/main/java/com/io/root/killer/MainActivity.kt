package com.io.root.killer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.io.root.killer.ui.MainScreen
import com.io.root.killer.ui.MainViewModel
import com.io.root.killer.ui.theme.RootAppKillerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RootAppKillerApp()
        }
    }
}

@Composable
private fun RootAppKillerApp() {
    RootAppKillerTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            val viewModel: MainViewModel = viewModel()
            MainScreen(viewModel = viewModel)
        }
    }
}
