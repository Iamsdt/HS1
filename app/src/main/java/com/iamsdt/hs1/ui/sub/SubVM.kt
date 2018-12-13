/*
 * Copyright (c) Shudipto Trafder
 * Created on 12/7/18 6:00 PM.
 */

package com.iamsdt.hs1.ui.sub

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.google.firebase.firestore.FirebaseFirestore
import com.iamsdt.hs1.db.Repository
import com.iamsdt.hs1.db.table.SubCategoryTable
import com.iamsdt.hs1.ext.SingleLiveEvent
import com.iamsdt.hs1.ui.cat.MainVM
import com.iamsdt.hs1.utils.SubcatDB
import com.iamsdt.hs1.utils.ioThread
import com.iamsdt.hs1.utils.model.EventMessage
import timber.log.Timber

class SubVM(private val repository: Repository) : ViewModel() {

    val dialogStatus = SingleLiveEvent<EventMessage>()

    fun add(txt: String, catID: Int) {

        val cat = SubCategoryTable(sub = txt, categoryID = catID)

        val store = FirebaseFirestore.getInstance().collection(SubcatDB.NAME)

        store.add(cat).addOnCompleteListener {
            if (it.isSuccessful) Timber.i("Sub category uploaded")
            else Timber.i("Sub category failed")
        }

        ioThread {

            val int = repository.addSubCat(cat)

            if (int > 0) {
                dialogStatus.postValue(EventMessage("Inserted Successfully", 1))
            }
        }
    }

    fun getAllCategory(id: Int = 0): LiveData<PagedList<SubCategoryTable>> {

        val source = if (id == 0) repository.getAllSubcategory()
        else repository.getSubListWithCatID(id)

        return LivePagedListBuilder(source, MainVM.PAGE_CONFIG).build()
    }
}
