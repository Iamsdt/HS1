package com.iamsdt.hs1.db.dao

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.*
import com.iamsdt.hs1.db.table.CategoryTable

@Dao
interface CategoryDao {

    @Insert
    fun add(myTable: CategoryTable): Long

    @Update
    fun update(myTable: CategoryTable): Int

    @Delete
    fun delete(myTable: CategoryTable): Int

    @Query("Select cat From CategoryTable")
    fun getAllCategories(): DataSource.Factory<Int, String>

    @Query("Select * From CategoryTable where id = :id")
    fun getCategory(id: String): LiveData<CategoryTable>

    @Query("Select * From CategoryTable")
    fun getAllData(): DataSource.Factory<Int, CategoryTable>
}