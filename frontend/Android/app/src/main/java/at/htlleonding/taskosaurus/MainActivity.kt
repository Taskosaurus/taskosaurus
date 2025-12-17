package at.htlleonding.taskosaurus

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import at.htlleonding.taskosaurus.ui.theme.TaskosaurusTheme
import at.htlleonding.taskosaurus.view.screens.MainScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TaskosaurusTheme {
                MainScreen()
            }
        }
    }
}