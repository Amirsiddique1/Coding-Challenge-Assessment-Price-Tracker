package com.apprays.real_timepricetrackerapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.apprays.real_timepricetrackerapp.navigation.AppNavigation
import com.apprays.real_timepricetrackerapp.ui.theme.RealTimePriceTrackerAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RealTimePriceTrackerAppTheme {
                AppNavigation()
            }
        }
    }
}
