package com.mk.permissionsconflict

import android.Manifest
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mk.permissionsconflict.ui.theme.PermissionsRequestsTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PermissionsRequestsTheme (
                dynamicColor = false
            ) {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Content()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Content() {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Text("Top app bar")
                }
            )
        },
    ) { contentPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
                .padding(top = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            SameTime(modifier = Modifier.padding(10.dp))
            MultiRequest(modifier = Modifier.padding(10.dp))
        }
    }

}

@Composable
fun SameTime(modifier: Modifier) {
    val localContext = LocalContext.current
    val notificationsResultHandler = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        val msg = "Notification permission grant: $isGranted"
        Toast.makeText(localContext, msg, Toast.LENGTH_SHORT).show()
        Log.d("!!!PR", msg)

    }
    val locationResultHandler = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        val msg = "Location permission grant: $isGranted"
        Toast.makeText(localContext, msg, Toast.LENGTH_SHORT).show()
        Log.d("!!!PR", msg)
    }

    Box(modifier = modifier) {
        Button(onClick = {
            notificationsResultHandler.launch(Manifest.permission.POST_NOTIFICATIONS)
            locationResultHandler.launch(Manifest.permission.ACCESS_COARSE_LOCATION)
        }) {
            Text(
                text = "Run separate requests",
                modifier = modifier
            )
        }
    }
}

@Composable
fun MultiRequest(modifier: Modifier) {
    val localContext = LocalContext.current
    val multiRequestHandler = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { grants ->
        grants.forEach {
            val msg = "${it.key} permission grant: ${it.value}"
            Toast.makeText(localContext, msg, Toast.LENGTH_SHORT).show()
            Log.d("!!!PR", "${it.key} permission grant: ${it.value}")
        }
    }

    Box(modifier = modifier) {
        Button(onClick = {
            multiRequestHandler.launch(
                arrayOf(
                    Manifest.permission.POST_NOTIFICATIONS,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }) {
            Text(
                text = "Run multi request",
                modifier = modifier
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    PermissionsRequestsTheme {
        Content()
    }
}