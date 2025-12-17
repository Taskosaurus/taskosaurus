package at.htlleonding.taskosaurus.view.screens.general

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import at.htlleonding.taskosaurus.viewModel.whoWouldRather.ViewModel

@Composable
fun SettingsScreen(viewModel: ViewModel = viewModel()){
    Text("Das is da setting screen yeah")
}