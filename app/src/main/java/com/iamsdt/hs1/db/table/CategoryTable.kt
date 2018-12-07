package com.iamsdt.hs1.db.table

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CategoryTable(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var cat: String = ""
)