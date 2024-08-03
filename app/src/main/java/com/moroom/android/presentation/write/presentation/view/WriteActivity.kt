package com.moroom.android.presentation.write.presentation.view;

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.moroom.android.presentation.write.presentation.view.screen.WriteScreen
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
class WriteActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WriteScreen()
        }
    }
}