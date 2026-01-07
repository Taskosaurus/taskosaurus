package at.htlleonding.taskosaurus.view.screens.whoWouldRather

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import at.htlleonding.taskosaurus.view.components.GroupListItem
import at.htlleonding.taskosaurus.viewModel.whoWouldRather.ViewModel

@Composable
fun GameListScreen(viewModel: ViewModel = viewModel()){
    val groups = viewModel.groups.collectAsState()

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(groups.value) { group ->
            GroupListItem(group)
        }
    }
}