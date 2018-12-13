package com.iamsdt.hs1.db.table

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class SubCategoryTable(
        @PrimaryKey(autoGenerate = true)
        var id: Int = 0,
        var categoryID: Int = 0,
        var sub: String = "",
        var ref: String = ""
)