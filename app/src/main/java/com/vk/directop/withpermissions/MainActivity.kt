package com.vk.directop.withpermissions

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.vk.directop.withpermissions.ui.theme.WithPermissionsTheme

class MainActivity : ComponentActivity() {
    @ExperimentalPermissionsApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WithPermissionsTheme {
                val permissionsState = rememberMultiplePermissionsState(
                    permissions = listOf(
                        Manifest.permission.RECORD_AUDIO,
                        Manifest.permission.CAMERA
                    )
                )

                val lifecycleOwner = LocalLifecycleOwner.current
                DisposableEffect(
                    key1 = lifecycleOwner,
                    effect = {
                        val observer = LifecycleEventObserver { _, event ->
                            if (event == Lifecycle.Event.ON_RESUME) {
                                permissionsState.launchMultiplePermissionRequest()
                            }
                        }
                        lifecycleOwner.lifecycle.addObserver(observer)

                        onDispose {
                            lifecycleOwner.lifecycle.removeObserver(observer)
                        }
                    }
                )


                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    permissionsState.permissions.forEach { perm ->
                        when (perm.permission) {
                            Manifest.permission.CAMERA -> {
                                when {
                                    perm.hasPermission -> {
                                        Text(text = "Camera permission accepted.",
                                            modifier = Modifier.padding(10.dp))
                                    }
                                    perm.shouldShowRationale -> {
                                        Text(
                                            text = "Camera permission is needed" +
                                                    "to access the camera.",
                                            modifier = Modifier.padding(10.dp)
                                        )
                                    }
                                    perm.isPermanentlyDenied() -> {
                                        Text(
                                            text = "Camera permission was permanently" +
                                                    " denied. You can enable it in the app " +
                                                    "settings.",
                                            modifier = Modifier.padding(10.dp)
                                        )
                                    }
                                }
                            }
                            Manifest.permission.RECORD_AUDIO -> {
                                when {
                                    perm.hasPermission -> {
                                        Text(text = "Record audio permission accepted.",
                                            modifier = Modifier.padding(10.dp))
                                    }
                                    perm.shouldShowRationale -> {
                                        Text(
                                            text = "Record audio permission is needed" +
                                                    "to access the camera.",
                                            modifier = Modifier.padding(10.dp)
                                        )
                                    }
                                    perm.isPermanentlyDenied() -> {
                                        Text(
                                            text = "Record audio permission was permanently" +
                                                    " denied. You can enable it in the app " +
                                                    "settings.",
                                            modifier = Modifier.padding(10.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                    //Text(text = "Record audio permission accepted")
                }

                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    Greeting("Android jet pack PV compose")
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(
        text = "Hello $name! Work with permissions.",
        modifier = Modifier.padding(10.dp))
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    WithPermissionsTheme {
        Greeting("Android")
    }
}