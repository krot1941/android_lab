package com.example.loversdiary.data

import android.net.Uri
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "photos")
@Parcelize
data class Photo (
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val uri: Uri? = null
) : Parcelable {}