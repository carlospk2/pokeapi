package com.example.pokeapi.shared.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.pokeapi.shared.database.entity.TrainerEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TrainerDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(trainers: List<TrainerEntity>)

    @Query("SELECT * FROM trainers ORDER BY difficultyTier ASC, id ASC")
    fun getAll(): Flow<List<TrainerEntity>>

    @Query("SELECT * FROM trainers WHERE id = :id")
    suspend fun getById(id: Int): TrainerEntity?

    @Query("SELECT * FROM trainers WHERE difficultyTier = :tier ORDER BY id ASC")
    fun getByTier(tier: Int): Flow<List<TrainerEntity>>

    @Query("SELECT COUNT(*) FROM trainers")
    suspend fun count(): Int
}
