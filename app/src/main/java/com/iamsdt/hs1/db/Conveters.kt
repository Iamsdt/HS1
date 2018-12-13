package com.iamsdt.hs1.db

import androidx.room.TypeConverter
import com.iamsdt.hs1.utils.PostType

class Conveters {

    @TypeConverter
    fun fromType(type: PostType): String = when (type) {
        PostType.LINK -> "${PostType.LINK}"
        PostType.IMAGE -> "${PostType.IMAGE}"
        PostType.VIDEO -> "${PostType.VIDEO}"
    }


    @TypeConverter
    fun toType(str: String): PostType = when (str) {
        "${PostType.LINK}" -> PostType.LINK
        "${PostType.VIDEO}" -> PostType.VIDEO
        "${PostType.IMAGE}" -> PostType.IMAGE
        else -> PostType.LINK
    }

}