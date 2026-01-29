package at.htlleonding.taskosaurus.data.local

import android.content.Context
import at.htlleonding.taskosaurus.data.model.Player

object PlayerPrefs {
    private const val PREFS_NAME = "taskosaurus_prefs"
    private const val KEY_PLAYER_ID = "playerId"
    private const val KEY_PLAYER_NAME = "playerName"

    fun savePlayer(context: Context, player: Player) {
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit().apply {
            putInt(KEY_PLAYER_ID, player.id)
            putString(KEY_PLAYER_NAME, player.name)
            apply()
        }
    }

    fun getPlayerId(context: Context): Int {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .getInt(KEY_PLAYER_ID, 0)
    }

    fun getPlayerName(context: Context): String? {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .getString(KEY_PLAYER_NAME, null)
    }

    fun clearPlayer(context: Context) {
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit().apply {
            remove(KEY_PLAYER_ID)
            remove(KEY_PLAYER_NAME)
            apply()
        }
    }

    fun hasPlayer(context: Context): Boolean {
        return getPlayerId(context) != 0
    }
}