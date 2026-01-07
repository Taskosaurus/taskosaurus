package at.htlleonding.taskosaurus.viewModel.whoWouldRather

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import at.htlleonding.taskosaurus.data.model.Group
import at.htlleonding.taskosaurus.data.remote.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ViewModel : ViewModel() {
    private val _groups = MutableStateFlow<List<Group>>(emptyList())
    val groups: StateFlow<List<Group>> get() = _groups

    init {
        fetchGroups()
    }

    private fun fetchGroups() {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.getGroups()
                _groups.value = response
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}