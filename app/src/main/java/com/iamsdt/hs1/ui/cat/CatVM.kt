/*
 * Copyright (c) Shudipto Trafder
 * Created on 12/7/18 12:30 PM.
 */

package com.iamsdt.hs1.ui.cat

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.google.firebase.firestore.FirebaseFirestore
import com.iamsdt.hs1.db.Repository
import com.iamsdt.hs1.db.table.CategoryTable
import com.iamsdt.hs1.ext.SingleLiveEvent
import com.iamsdt.hs1.utils.CatDB
import com.iamsdt.hs1.utils.ioThread
import com.iamsdt.hs1.utils.model.EventMessage
import timber.log.Timber

class CatVM(private val repository: Repository) : ViewModel() {


    val status = SingleLiveEvent<EventMessage>()

    fun getAllCategory(): LiveData<PagedList<CategoryTable>> {
        val source = repository.getAllCategory()

        return LivePagedListBuilder(source, PAGE_CONFIG).build()
    }

    fun add(txt: String) {

        val cat = CategoryTable(cat = txt)

        val ref =
                FirebaseFirestore.getInstance().collection(CatDB.NAME).document(txt)

        ref.set(cat).addOnCompleteListener {
            if (it.isSuccessful) Timber.i("category uploaded")
            else Timber.i("category failed")
        }

        ioThread {
            val id = repository.addCat(cat)
            if (id > 0L)
                status.postValue(EventMessage("Added Successfully", 1))
        }
    }


    companion object {

        val PAGE_CONFIG: PagedList.Config = PagedList.Config.Builder()
                .setPageSize(30)
                .setInitialLoadSizeHint(50)//by default page size * 3
                .setPrefetchDistance(20) // default page size
                .setEnablePlaceholders(true) //default true
                .build()
    }
}