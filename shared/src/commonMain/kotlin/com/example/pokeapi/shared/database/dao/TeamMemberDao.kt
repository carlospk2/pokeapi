package com.example.pokeapi.shared.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.pokeapi.shared.database.entity.TeamMemberEntity

@Dao
interface TeamMemberDao {

    @Insert
    suspend fun insert(member: TeamMemberEntity): Long

    @Update
    suspend fun update(member: TeamMemberEntity)

    @Delete
    suspend fun delete(member: TeamMemberEntity)

    @Query("SELECT * FROM team_members WHERE teamId = :teamId ORDER BY slot ASC")
    suspend fun getMembersForTeam(teamId: Long): List<TeamMemberEntity>

    @Query("DELETE FROM team_members WHERE teamId = :teamId")
    suspend fun deleteMembersForTeam(teamId: Long)
}
