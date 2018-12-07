package com.iamsdt.hs1.db

import com.iamsdt.hs1.db.dao.CategoryDao
import com.iamsdt.hs1.db.dao.MyTableDao
import com.iamsdt.hs1.db.dao.SubCategoryDao
import com.iamsdt.hs1.db.table.MyTable

class Repository(
    private val myTableDao: MyTableDao,
    private val subCategoryDao: SubCategoryDao,
    private val categoryDao: CategoryDao
) {

    val allData = myTableDao.getAllData()

    fun getTypeData(string: String) = myTableDao.getType(string)

    fun getCategoryData(string: String) = myTableDao.getCategory(string)

    fun getSubCategoryData(string: String) = myTableDao.getSubCategory(string)

    fun getCategorySubData(cat: String, sub: String) = myTableDao.getCategorySubCategory(cat, sub)

    fun add(myTable: MyTable) = myTableDao.add(myTable)
    fun update(myTable: MyTable) = myTableDao.update(myTable)
    fun delete(myTable: MyTable) = myTableDao.delete(myTable)


    fun getAllCategory() = categoryDao.getAllData()

    fun getCatCount(id: Int) = myTableDao.getCatIDs(id)
}