package com.example.loversdiary.data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "events")
@Parcelize
data class Event (
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String = ""
) : Parcelable {}