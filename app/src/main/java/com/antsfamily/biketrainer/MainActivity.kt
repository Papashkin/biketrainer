package com.antsfamily.biketrainer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import com.antsfamily.biketrainer.navigation.Navigation
import com.antsfamily.biketrainer.presentation.main.MainViewModel
import com.antsfamily.biketrainer.ui.util.AppTheme
//import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainContent()
        }
    }
}

@Composable
fun MainContent(
    viewModel: MainViewModel = hiltViewModel()
) {
    val state = viewModel.state.collectAsState()
//    val systemUiController = rememberSystemUiController()

    AppTheme(state.value) {
//        systemUiController.setSystemBarsColor(color = MaterialTheme.colorScheme.surface)
        Navigation()
    }
}
