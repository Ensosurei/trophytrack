package org.ensosurei.trophytrack.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface GameNotesDao {
    @Upsert
    suspend fun saveNote(note: GameNotesEntity)

    @Query("SELECT * FROM GameNotesEntity WHERE gameId= :id")
    fun getNotes(id:Int): Flow<List<GameNotesEntity>>

    @Query("DELETE FROM GameNotesEntity WHERE id= :id")
    suspend fun deleteNote(id: Int)

    @Query("DELETE FROM GameNotesEntity WHERE gameId= :gameId")
    suspend fun deleteNotesByGameId(gameId: Int)
}