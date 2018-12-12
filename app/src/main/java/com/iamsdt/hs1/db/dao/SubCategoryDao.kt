package com.iamsdt.hs1.db.dao

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.*
import com.iamsdt.hs1.db.table.SubCategoryTable

@Dao
interface SubCategoryDao {

    @Insert
    fun add(myTable: SubCategoryTable): Long

    @Update
    fun update(myTable: SubCategoryTable): Int

    @Delete
    fun delete(myTable: SubCategoryTable): Int

    @Query("Select sub From SubCategoryTable")
    fun getAllSubcategories(): DataSource.Factory<Int, String>

    @Query("Select * From SubCategoryTable where id = :id")
    fun getSubcategory(id: String): LiveData<SubCategoryTable>

    @Query("Select * From SubCategoryTable")
    fun getAllData(): DataSource.Factory<Int, SubCategoryTable>

    @Query("Select * From SubCategoryTable where id= :id")
    fun getSubCat(id: Int): SubCategoryTable

    @Query("Select * From SubCategoryTable where categoryID=:id")
    fun getIDCatSub(id: Int): DataSource.Factory<Int, SubCategoryTable>

}