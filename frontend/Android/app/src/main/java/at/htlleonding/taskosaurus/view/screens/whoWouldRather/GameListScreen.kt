package at.htlleonding.taskosaurus.view.screens.whoWouldRather

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import at.htlleonding.taskosaurus.viewModel.whoWouldRather.ViewModel

@Composable
fun GameListScreen(viewModel: ViewModel = viewModel()){
    Text("Das is de game list screen yeah")
}