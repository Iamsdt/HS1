package com.iamsdt.hs1.db

import com.iamsdt.hs1.db.dao.CategoryDao
import com.iamsdt.hs1.db.dao.MyTableDao
import com.iamsdt.hs1.db.dao.SubCategoryDao
import com.iamsdt.hs1.db.table.CategoryTable
import com.iamsdt.hs1.db.table.MyTable
import com.iamsdt.hs1.db.table.SubCategoryTable

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

    fun addMyTable(myTable: MyTable) = myTableDao.add(myTable)
    fun updateMyTable(myTable: MyTable) = myTableDao.update(myTable)
    fun deleteMyTable(myTable: MyTable) = myTableDao.delete(myTable)

    fun addCat(cat: CategoryTable) = categoryDao.add(cat)
    fun updateCat(cat: CategoryTable) = categoryDao.add(cat)
    fun deleteCat(cat: CategoryTable) = categoryDao.add(cat)

    fun addSubCat(sub: SubCategoryTable) = subCategoryDao.add(sub)
    fun updateSubCat(sub: SubCategoryTable) = subCategoryDao.add(sub)
    fun deleteSubCat(sub: SubCategoryTable) = subCategoryDao.add(sub)

    fun getAllCategory() = categoryDao.getAllData()
    fun getAllSubcategory() = subCategoryDao.getAllData()

    fun getCatCount(id: Int) = myTableDao.getCatIDs(id)
}