package com.sulthoni.challange_chapter_4.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "note_table")
@Parcelize
data class Notes(
    @PrimaryKey (autoGenerate = true)
    var id: Int?,
    @ColumnInfo(name="title")
    var title: String?,
    @ColumnInfo(name="data")
    var data: String?
):Parcelable
