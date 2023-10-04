package com.sam.networklogger

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.sam.network_logger.ui.bottomsheet.LogsSheet
import com.sam.networklogger.ui.MainViewModel
import com.sam.networklogger.ui.theme.NetworkLoggerTheme

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NetworkLoggerTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    LaunchedEffect("Android") {
                        viewModel.makeAPICall(applicationContext)
                    }
                    Box(modifier = Modifier.fillMaxWidth()) {
                        Button(
                            modifier = Modifier.align(Alignment.Center),
                            onClick = { viewModel.postData(applicationContext) }
                        ) {
                            Text(text = "adakdsjfasdfkj")
                        }
                    }
                    LogsSheet(
                        materialTheme = MaterialTheme
                    )
                }
            }
        }
    }
}