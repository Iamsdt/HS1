/*
 * Copyright (c) Shudipto Trafder
 * Created on 12/7/18 6:00 PM.
 */

package com.iamsdt.hs1.ui.sub

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.iamsdt.hs1.db.Repository
import com.iamsdt.hs1.db.table.SubCategoryTable
import com.iamsdt.hs1.ext.SingleLiveEvent
import com.iamsdt.hs1.ui.main.MainVM
import com.iamsdt.hs1.utils.ioThread
import com.iamsdt.hs1.utils.model.EventMessage

class SubVM(private val repository: Repository) : ViewModel() {

    val dialogStatus = SingleLiveEvent<EventMessage>()

    fun add(txt: String, catID: Int) {
        //todo add firestore
        ioThread {
            val cat = SubCategoryTable(sub = txt, categoryID = catID)
            val int = repository.addSubCat(cat)

            if (int > 0) {
                dialogStatus.postValue(EventMessage("Inserted Successfully", 1))
            }
        }
    }

    fun getAllCategory(): LiveData<PagedList<SubCategoryTable>> {
        val source = repository.getAllSubcategory()

        return LivePagedListBuilder(source, MainVM.PAGE_CONFIG).build()
    }

}