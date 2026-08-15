package com.example.pomodoroteam

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource

import pomodoroteam.app.shared.generated.resources.Res
import pomodoroteam.app.shared.generated.resources.compose_multiplatform

@Composable
@Preview
fun App() {
    MaterialTheme {
        var showContent by remember { mutableStateOf(false) }
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.primaryContainer)
                .safeContentPadding()
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceAround,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text("POMODORO")
            Text(
                modifier = Modifier.background(Color.Red).padding(8.dp),
                text = "FOCUS MODE",
                color = Color.White
            )
            Text("07:12")

            Text("Please don't disturb")

            LinearProgressIndicator(
                progress = { 0.7f },
                modifier = Modifier.padding(horizontal = 32.dp),
                color = Color.Green,
                trackColor = Color.LightGray,
            )

            Row(
                modifier = Modifier.padding(horizontal = 32.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                PomodoroButton("Pause") {}
                PomodoroButton("End") {}
            }
        }
    }
}

@Composable
fun PomodoroButton(text: String, onClick: () -> Unit) {
    Button(modifier = Modifier.padding(8.dp).width(128.dp), onClick = onClick) {
        Text(text)
    }
}