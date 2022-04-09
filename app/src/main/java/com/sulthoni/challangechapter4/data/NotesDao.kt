package com.example.roomapp.data

import androidx.room.*
import com.sulthoni.challange_chapter_4.model.Notes

@Dao
interface NotesDao {

    @Query("SELECT * FROM note_table")
    fun getAllNotes(): List<Notes>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertData(data: Notes):Long

    @Update
    fun updateData(data: Notes):Int

    @Delete
    fun deleteData(data: Notes):Int

}