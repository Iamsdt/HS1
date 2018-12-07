package com.iamsdt.hs1.db.table

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class SubCategoryTable(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val categoryID: Int,
    val sub: String
)