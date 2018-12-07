package com.iamsdt.hs1.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.iamsdt.hs1.db.dao.CategoryDao
import com.iamsdt.hs1.db.dao.MyTableDao
import com.iamsdt.hs1.db.dao.SubCategoryDao
import com.iamsdt.hs1.db.table.CategoryTable
import com.iamsdt.hs1.db.table.MyTable
import com.iamsdt.hs1.db.table.SubCategoryTable

@Database(
    entities = [MyTable::class, CategoryTable::class, SubCategoryTable::class],
    version = 1, exportSchema = false
)
abstract class MyDatabase() : RoomDatabase() {
    abstract val myTableDao: MyTableDao
    abstract val subCategoryDao: SubCategoryDao
    abstract val categoryDao: CategoryDao
}