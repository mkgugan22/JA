package com.mkgugan.jarvis

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mkgugan.jarvis.data.remote.HealthApi
import com.mkgugan.jarvis.data.remote.HealthResult
import kotlinx.coroutines.launch

private sealed interface CheckState {
    data object Idle : CheckState
    data object Loading : CheckState
    data object Online : CheckState
    data class Unavailable(val reason: String) : CheckState
}

class MainActivity : ComponentActivity() {
    private val healthApi = HealthApi()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    ConnectivityScreen(healthApi)
                }
            }
        }
    }
}

@Composable
private fun ConnectivityScreen(healthApi: HealthApi) {
    var state by remember { mutableStateOf<CheckState>(CheckState.Idle) }
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
    ) {
        Text(
            text = "JARVIS — Phase 1A",
            style = MaterialTheme.typography.headlineSmall,
        )
        Text(
            text = statusText(state),
            style = MaterialTheme.typography.bodyLarge,
        )

        if (state is CheckState.Loading) {
            CircularProgressIndicator()
        }

        Button(onClick = {
            state = CheckState.Loading
            scope.launch {
                state = when (val result = healthApi.checkHealth()) {
                    is HealthResult.Online -> CheckState.Online
                    is HealthResult.Unavailable -> CheckState.Unavailable(result.reason)
                }
            }
        }) {
            Text("Check backend")
        }
    }
}

private fun statusText(state: CheckState): String = when (state) {
    CheckState.Idle -> "Not checked yet."
    CheckState.Loading -> "Checking backend…"
    CheckState.Online -> "Backend online"
    is CheckState.Unavailable -> "Backend unavailable (${state.reason}). You can retry."
}
