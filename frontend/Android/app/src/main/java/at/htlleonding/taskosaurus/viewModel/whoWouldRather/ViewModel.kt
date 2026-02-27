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
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class ViewModel(application: Application) : AndroidViewModel(application) {

    private val _randomQuestions = MutableStateFlow<List<Question>>(emptyList())
    val randomQuestions: StateFlow<List<Question>> get() = _randomQuestions

    private val _groups = MutableStateFlow<List<Group>>(emptyList())
    val groups: StateFlow<List<Group>> get() = _groups

    private val _latestQuestions = MutableStateFlow<Map<Int, Question>>(emptyMap())
    val latestQuestions: StateFlow<Map<Int, Question>> get() = _latestQuestions

    private val _hasConnection = MutableStateFlow(true)
    val hasConnection: StateFlow<Boolean> get() = _hasConnection

    private val _isReady = MutableStateFlow(false)
    val isReady: StateFlow<Boolean> get() = _isReady
    private val _player = MutableStateFlow<Player?>(null)
    val player: StateFlow<Player?> get() = _player

    private val _playerId = MutableStateFlow(0)
    val playerId: StateFlow<Int> get() = _playerId
    private var autoRefreshJob: Job? = null
    val unAnsweredGroups: StateFlow<List<Group>> = combine(_groups, _latestQuestions) { groups, questions ->
        groups.filter { group ->
            val question = questions[group.id]
            question != null && !question.answered
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val answeredGroups: StateFlow<List<Group>> = combine(_groups, _latestQuestions) { groups, questions ->
        groups.filter { group ->
            val question = questions[group.id]
            question?.answered == true
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        val savedPlayerId = PlayerPrefs.getPlayerId(application)
        if (savedPlayerId != 0) {
            _playerId.value = savedPlayerId
            loadPlayerFromId(savedPlayerId)
        } else {
            loadPlayerFromId(1)
        }
        fetchRandomQuestions(8)
    }

    fun startAutoRefresh() {
        fetchGroups()
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

    fun createAndSaveUser(playerDto: PlayerNameDto, onSuccess: (Player) -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            try {
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

    fun loadPlayerFromDto(request: PlayerNameDto, onSuccess: (Player) -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            try {
                val loadedPlayer = RetrofitInstance.playerApi.getPlayerByDto(request)
                PlayerPrefs.savePlayer(getApplication(), Player(id = loadedPlayer.id, name = ""))
                _player.value = loadedPlayer
                _playerId.value = loadedPlayer.id
                _hasConnection.value = true
                onSuccess(loadedPlayer)
            } catch (e: Exception) {
                Log.e("ViewModel", "Error loading player", e)
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
                _isReady.value = true
            } catch (e: Exception) {
                Log.e("ViewModel", "Error loading player", e)
                _isReady.value = true
                _hasConnection.value = false
            }
        }
    }

    fun fetchRandomQuestions(amount: Int) {
        viewModelScope.launch {
            try {
                val questions = RetrofitInstance.questionApi.getRandomQuestions(amount)
                _randomQuestions.value = questions
                _hasConnection.value = true
            } catch (e: Exception) {
                Log.e("ViewModel", "Error loading random questions", e)
                _hasConnection.value = false
            }
        }
    }

    private fun fetchGroups() {
        viewModelScope.launch {
            try {
                val player = _player.value ?: return@launch
                val groups = RetrofitInstance.groupApi.getJoinedGroups(player)
                _groups.value = groups
                _hasConnection.value = true
                loadQuestionsForGroups()
            } catch (e: Exception) {
                Log.e("ViewModel", "Error fetching groups", e)
                _hasConnection.value = false
            }
        }
    }

    private suspend fun loadQuestionsForGroups() {
        val playerId = _playerId.value
        _groups.value.forEach { group ->
            try {
                val request = DailyQuestionRequest(
                    id = playerId,
                    name = _player.value?.name ?: "",
                    groupId = group.id,
                    date = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE)
                )
                val question = RetrofitInstance.questionApi.getDailyQuestion(request)
                _latestQuestions.value = _latestQuestions.value + (group.id to question)
                _hasConnection.value = true
            } catch (e: Exception) {
                Log.e("ViewModel", "Error fetching question for group ${group.id}", e)
            }
        }
    }

    fun joinGroup(groupId: Int, onSuccess: () -> Unit = {}) {
        viewModelScope.launch {
            try {
                val currentPlayer = _player.value ?: return@launch
                RetrofitInstance.groupApi.joinGroup(groupId, currentPlayer.name)
                val updatedGroups = RetrofitInstance.groupApi.getJoinedGroups(currentPlayer)
                _groups.value = updatedGroups
                loadQuestionsForGroups()
                onSuccess()
            } catch (e: Exception) {
                Log.e("ViewModel", "Fehler beim Joinen von $groupId", e)
            }
        }
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