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

    fun getSearchList(str:String) = myTableDao.searchList(str)

    fun getCategoryData(string: String) = myTableDao.getCategory(string)

    fun getSubCategoryData(string: String) = myTableDao.getSubCategory(string)

    fun getCategorySubData(cat: String, sub: String) = myTableDao.getCategorySubCategory(cat, sub)

    fun getDetails(id: Int) = myTableDao.getDetails(id)

    fun addMyTable(myTable: MyTable) = myTableDao.add(myTable)
    fun updateMyTable(myTable: MyTable) = myTableDao.update(myTable)
    fun deleteMyTable(myTable: MyTable) = myTableDao.delete(myTable)

    fun getCatCount(id: Int) = myTableDao.getCatIDs(id)
    fun getSubcatCount(id: Int) = myTableDao.getSubcatIDs(id)


    fun addCat(cat: CategoryTable) = categoryDao.add(cat)
    fun updateCat(cat: CategoryTable) = categoryDao.update(cat)
    fun deleteCat(cat: CategoryTable) = categoryDao.delete(cat)

    fun getAllCategory() = categoryDao.getAllData()

    fun getCat(id: Int) = categoryDao.getCat(id)
    fun getCategory(id: Int) = categoryDao.getCategory(id)


    fun addSubCat(sub: SubCategoryTable) = subCategoryDao.add(sub)
    fun updateSubCat(sub: SubCategoryTable) = subCategoryDao.update(sub)
    fun deleteSubCat(sub: SubCategoryTable) = subCategoryDao.delete(sub)

    fun getAllSubcategory() = subCategoryDao.getAllData()
    fun getSubListWithCatID(int: Int) = subCategoryDao.getIDCatSub(int)

    fun getSubcat(id: Int) = subCategoryDao.getSubCat(id)
    fun getSubcategory(id: Int) = subCategoryDao.getSubcategory(id)


}