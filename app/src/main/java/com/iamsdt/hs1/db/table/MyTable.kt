package com.iamsdt.hs1.db.table

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.iamsdt.hs1.utils.PostType

@Entity
data class MyTable(
        @PrimaryKey(autoGenerate = true)
        var id: Int = 0,
        var title: String = "",
        var des: String = "",
        var type: PostType = PostType.LINK,
        var link: String = "",
        var img: String = "",
        var category: String = "",
        var categoryID: Int = 0,
        var subCategory: String = "",
        var subCategoryID: Int = 0,
        var ref: String = ""
)