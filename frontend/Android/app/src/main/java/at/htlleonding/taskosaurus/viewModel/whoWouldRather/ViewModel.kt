package at.htlleonding.taskosaurus.viewModel.whoWouldRather

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import at.htlleonding.taskosaurus.data.local.PlayerPrefs
import at.htlleonding.taskosaurus.data.model.*
import at.htlleonding.taskosaurus.data.remote.RetrofitInstance
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class ViewModel(application: Application) : AndroidViewModel(application) {

    // State flows
    private val _groups = MutableStateFlow<List<Group>>(emptyList())
    val groups: StateFlow<List<Group>> get() = _groups

    private val _latestQuestions = MutableStateFlow<Map<Int, Question>>(emptyMap())
    val latestQuestions: StateFlow<Map<Int, Question>> get() = _latestQuestions

    private val _hasConnection = MutableStateFlow(true)
    val hasConnection: StateFlow<Boolean> get() = _hasConnection

    private val _player = MutableStateFlow<Player?>(null)
    val player: StateFlow<Player?> get() = _player

    private val _playerId = MutableStateFlow(0)
    val playerId: StateFlow<Int> get() = _playerId

    private var autoRefreshJob: Job? = null

    // Computed properties like iOS
    val unAnsweredGroups: List<Group>
        get() = _groups.value.filter { group ->
            val question = _latestQuestions.value[group.id]
            question != null && !question.answered
        }

    val answeredGroups: List<Group>
        get() = _groups.value.filter { group ->
            val question = _latestQuestions.value[group.id]
            question?.answered == true
        }

    init {
        // 🔧 SPRINT DEMO: Immer Player ID 1!
        _playerId.value = 1
        PlayerPrefs.savePlayer(application, Player(id = 1, name = "DemoUser"))

        // 🔥 SOFORT laden beim Start!
        loadPlayerFromId(1)
    }

    fun startAutoRefresh() {
        // 🔥 Sofort einmal fetchen!
        if (_player.value != null) {
            fetchGroups()
        }

        autoRefreshJob?.cancel()
        autoRefreshJob = viewModelScope.launch {
            while (isActive) {
                delay(5000)
                fetchGroups()
            }
        }
    }

    fun stopAutoRefresh() {
        autoRefreshJob?.cancel()
    }

    fun loginWithId(id: Int) {
        _playerId.value = id
        PlayerPrefs.savePlayer(getApplication(), Player(id = id, name = ""))
        loadPlayerFromId(id)
    }

    fun createAndSaveUser(playerName: String, onSuccess: (Player) -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            try {
                val playerDto = PlayerNameDto(name = playerName)
                val createdPlayer = RetrofitInstance.playerApi.createPlayer(playerDto)

                _player.value = createdPlayer
                _playerId.value = createdPlayer.id
                PlayerPrefs.savePlayer(getApplication(), createdPlayer)
                _hasConnection.value = true

                onSuccess(createdPlayer)
            } catch (e: Exception) {
                Log.e("ViewModel", "Error creating player", e)
                _hasConnection.value = false
                onError(e.message ?: "Unknown error")
            }
        }
    }

    fun loadPlayerFromId(id: Int) {
        viewModelScope.launch {
            try {
                val loadedPlayer = RetrofitInstance.playerApi.getPlayerById(id)
                _player.value = loadedPlayer
                _playerId.value = id
                _hasConnection.value = true

                // 🔥 SOFORT nach Player-Load: Groups laden!
                fetchGroups()
            } catch (e: Exception) {
                Log.e("ViewModel", "Error loading player", e)
                _hasConnection.value = false
            }
        }
    }

    private fun fetchGroups() {
        viewModelScope.launch {
            try {
                val player = _player.value
                if (player == null) {
                    Log.w("ViewModel", "Cannot fetch groups: No player")
                    return@launch
                }

                Log.d("ViewModel", "Fetching groups for player: ${player.name}")
                val groups = RetrofitInstance.groupApi.getJoinedGroups(player)
                _groups.value = groups
                _hasConnection.value = true

                Log.d("ViewModel", "Loaded ${groups.size} groups")

                // Load questions for each group
                loadQuestionsForGroups()
            } catch (e: Exception) {
                Log.e("ViewModel", "Error fetching groups", e)
                _hasConnection.value = false
            }
        }
    }

    private suspend fun loadQuestionsForGroups() {
        val playerId = _playerId.value
        val playerName = _player.value?.name ?: ""

        Log.d("ViewModel", "Loading questions for ${_groups.value.size} groups")

        _groups.value.forEach { group ->
            try {
                val request = DailyQuestionRequest(
                    id = playerId,
                    name = playerName,
                    groupId = group.id,
                    date = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE)
                )

                val question = RetrofitInstance.questionApi.getDailyQuestion(request)
                _latestQuestions.value = _latestQuestions.value + (group.id to question)
                _hasConnection.value = true

                Log.d("ViewModel", "Loaded question for group ${group.name}")
            } catch (e: Exception) {
                Log.e("ViewModel", "Error fetching question for group ${group.id}", e)
            }
        }

        Log.d("ViewModel", "All questions loaded. Total: ${_latestQuestions.value.size}")
    }

    fun submitVote(
        groupId: Int,
        answeredPlayerId: Int,
        onSuccess: (Question) -> Unit = {},
        onError: (String) -> Unit = {}
    ) {
        viewModelScope.launch {
            try {
                val player = _player.value ?: run {
                    onError("Player not found")
                    return@launch
                }

                val answer = DailyQuestionAnswer(
                    playerId = player.id,
                    answerId = answeredPlayerId,
                    groupId = groupId,
                    date = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE)
                )

                val updatedQuestion = RetrofitInstance.questionApi.answerDailyQuestion(answer)
                _latestQuestions.value = _latestQuestions.value + (groupId to updatedQuestion)
                _hasConnection.value = true

                onSuccess(updatedQuestion)
            } catch (e: Exception) {
                Log.e("ViewModel", "Error submitting vote", e)
                _hasConnection.value = false
                onError(e.message ?: "Unknown error")
            }
        }
    }

    fun createGroup(name: String, onSuccess: (Group) -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            try {
                val player = _player.value ?: run {
                    onError("Player not found")
                    return@launch
                }

                val group = RetrofitInstance.groupApi.createGroup(name, player)
                _hasConnection.value = true

                // Refresh groups after creating
                fetchGroups()

                onSuccess(group)
            } catch (e: Exception) {
                Log.e("ViewModel", "Error creating group", e)
                _hasConnection.value = false
                onError(e.message ?: "Unknown error")
            }
        }
    }

    fun logout() {
        PlayerPrefs.clearPlayer(getApplication())
        _player.value = null
        _playerId.value = 0
        _groups.value = emptyList()
        _latestQuestions.value = emptyMap()
        stopAutoRefresh()
    }

    override fun onCleared() {
        super.onCleared()
        stopAutoRefresh()
    }
}