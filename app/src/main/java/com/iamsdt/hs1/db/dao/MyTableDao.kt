/*
 * Copyright (c) Shudipto Trafder
 * Created on 12/7/18 1:41 PM.
 */

package com.iamsdt.hs1.db.dao

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.*
import com.iamsdt.hs1.db.table.MyTable

@Dao
interface MyTableDao {

    @Insert
    fun add(myTable: MyTable): Long

    @Update
    fun update(myTable: MyTable): Int

    @Delete
    fun delete(myTable: MyTable): Int

    @Query("Select * From MyTable where category = :cat")
    fun getCategory(cat: String): DataSource.Factory<Int, MyTable>

    @Query("Select * From MyTable where title like :str||'%'")
    fun searchList(str: String): DataSource.Factory<Int, MyTable>

    @Query("Select * From MyTable where subCategory = :cat")
    fun getSubCategory(cat: String): DataSource.Factory<Int, MyTable>

    @Query("Select * From MyTable where category = :cat AND subCategory =:sub")
    fun getCategorySubCategory(cat: String, sub: String): DataSource.Factory<Int, MyTable>

    @Query("Select * From MyTable")
    fun getAllData(): DataSource.Factory<Int, MyTable>

    @Query("Select * From MyTable where type = :type")
    fun getType(type: String): DataSource.Factory<Int, MyTable>

    @Query("Select link From MyTable where id =:id")
    fun getLink(id: Int): LiveData<String>

    @Query("Select img From MyTable where id =:id")
    fun getImg(id: Int): LiveData<String>

    @Query("Select id From MyTable where categoryID = :id")
    fun getCatIDs(id: Int): List<Int>

    @Query("Select id From MyTable where subCategoryID = :id")
    fun getSubcatIDs(id: Int): List<Int>

    @Query("Select * From MyTable where title = :str")
    fun search(str: String): MyTable


    @Query("Select * From MyTable where id = :id")
    fun getDetails(id: Int): LiveData<MyTable>
}