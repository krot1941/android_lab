package com.example.loversdiary.data

import android.graphics.Bitmap
import android.os.Build
import android.os.Parcelable
import androidx.annotation.RequiresApi
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import java.text.SimpleDateFormat
import java.util.*

@Entity(tableName = "user")
@Parcelize
data class User (
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val user_name: String = "",
    val partner_name: String = "",
    val date_of_start: Long = System.currentTimeMillis()
) : Parcelable {
    val dateOfStartFormatted: String
        @RequiresApi(Build.VERSION_CODES.O) get() = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault()).format(
            Date(date_of_start)
        )
    val dateOfStartFormattedAsYear: String
        @RequiresApi(Build.VERSION_CODES.O) get() = SimpleDateFormat("yyyy", Locale.getDefault()).format(
            Date(date_of_start)
        )
    val dateOfStartFormattedAsMonth: String
        @RequiresApi(Build.VERSION_CODES.O) get() = SimpleDateFormat("MM", Locale.getDefault()).format(
            Date(date_of_start)
        )
    val dateOfStartFormattedAsDay: String
        @RequiresApi(Build.VERSION_CODES.O) get() = SimpleDateFormat("dd", Locale.getDefault()).format(
            Date(date_of_start)
        )
}