package com.example.loversdiary.data

import android.os.Build
import android.os.Parcelable
import androidx.annotation.RequiresApi
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import java.text.SimpleDateFormat
import java.util.*

@Entity(tableName = "moments", foreignKeys = [ForeignKey(entity = Event::class, parentColumns = ["id"], childColumns = ["event_id"])])
@Parcelize
data class Moment (
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val place: String = "",
    val date: Long = System.currentTimeMillis(),
    val note: String = "",
    val event_id: Int = 0
) : Parcelable {

    val dateFormatted: String
        @RequiresApi(Build.VERSION_CODES.O) get() = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault()).format(
                Date(date)
        )

    val dateFormattedAsYear: String
        @RequiresApi(Build.VERSION_CODES.O) get() = SimpleDateFormat("yyyy", Locale.getDefault()).format(
                Date(date)
        )
    val dateFormattedAsMonth: String
        @RequiresApi(Build.VERSION_CODES.O) get() = SimpleDateFormat("MM", Locale.getDefault()).format(
                Date(date)
        )
    val dateFormattedAsDay: String
        @RequiresApi(Build.VERSION_CODES.O) get() = SimpleDateFormat("dd", Locale.getDefault()).format(
                Date(date)
        )
}