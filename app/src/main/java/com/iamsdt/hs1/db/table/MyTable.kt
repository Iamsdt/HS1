package com.iamsdt.hs1.db.table

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.iamsdt.hs1.utils.PostType

@Entity
data class MyTable(
        @PrimaryKey(autoGenerate = true)
        val id: Int,
        val title: String,
        val des: String,
        val type: PostType,
        val link: String,
        val img: String,
        val category: String,
        val categoryID: Int,
        val subCategory: String,
        val subCategoryID: Int
)