package com.iamsdt.hs1.db.table

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class MyTable(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val title: String,
    val type: String,
    val link: String,
    val img: String,
    val category: String,
    val categoryID: Int,
    val subCategory: String,
    val subCategoryID: Int
)