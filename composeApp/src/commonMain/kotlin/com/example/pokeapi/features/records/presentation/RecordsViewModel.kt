package com.example.pokeapi.features.records.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokeapi.shared.database.dao.BattleRecordDao
import com.example.pokeapi.shared.database.dao.PlayerStatsDao
import com.example.pokeapi.shared.database.entity.PlayerStatsEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update

data class PlayerStatsDisplay(
    val totalWins: Int = 0,
    val totalLosses: Int = 0,
    val currentStreak: Int = 0,
    val bestStreak: Int = 0,
    val totalBattles: Int = 0
)

data class BattleHistoryItem(
    val id: Long,
    val trainerId: Int,
    val result: String,
    val turnsCount: Int,
    val date: Long
)

data class RecordsState(
    val playerStats: PlayerStatsDisplay = PlayerStatsDisplay(),
    val recentBattles: List<BattleHistoryItem> = emptyList(),
    val isLoading: Boolean = true
)

class RecordsViewModel(
    private val battleRecordDao: BattleRecordDao,
    private val playerStatsDao: PlayerStatsDao
) : ViewModel() {

    private val _state = MutableStateFlow(RecordsState())
    val state: StateFlow<RecordsState> = _state.asStateFlow()

    init {
        battleRecordDao.getAll()
            .onEach { records ->
                val stats = playerStatsDao.get() ?: PlayerStatsEntity()
                _state.update {
                    it.copy(
                        playerStats = PlayerStatsDisplay(
                            totalWins = stats.totalWins,
                            totalLosses = stats.totalLosses,
                            currentStreak = stats.currentStreak,
                            bestStreak = stats.bestStreak,
                            totalBattles = stats.totalBattles
                        ),
                        recentBattles = records.take(20).map { r ->
                            BattleHistoryItem(
                                id = r.id,
                                trainerId = r.trainerId,
                                result = r.result,
                                turnsCount = r.turnsCount,
                                date = r.date
                            )
                        },
                        isLoading = false
                    )
                }
            }
            .launchIn(viewModelScope)
    }
}
