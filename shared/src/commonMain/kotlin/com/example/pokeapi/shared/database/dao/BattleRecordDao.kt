package com.example.pokeapi.shared.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.pokeapi.shared.database.entity.BattleRecordEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BattleRecordDao {

    @Insert
    suspend fun insert(record: BattleRecordEntity): Long

    @Query("SELECT * FROM battle_records ORDER BY date DESC")
    fun getAll(): Flow<List<BattleRecordEntity>>

    @Query("SELECT * FROM battle_records WHERE trainerId = :trainerId ORDER BY date DESC")
    fun getByTrainer(trainerId: Int): Flow<List<BattleRecordEntity>>
}
