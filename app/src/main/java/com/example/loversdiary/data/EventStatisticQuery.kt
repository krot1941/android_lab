package com.example.loversdiary.data

import androidx.room.ColumnInfo

data class EventStatisticQuery (
    @ColumnInfo(name = "name")
    var name: String? = null,

    @ColumnInfo(name = "count")
    var count: Int? = null
)