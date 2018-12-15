package com.iamsdt.hs1.ui.main

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.iamsdt.hs1.db.Repository
import com.iamsdt.hs1.db.table.MyTable
import com.iamsdt.hs1.ui.cat.CatVM

class MainVM(private val repository: Repository) : ViewModel() {

    val data = MediatorLiveData<PagedList<MyTable>>()

    fun search(string: String): MediatorLiveData<PagedList<MyTable>> {

        val fa = repository.getSearchList(string)
        val src = LivePagedListBuilder(fa, CatVM.PAGE_CONFIG).build()
        data.addSource(src) {
            data.value = it
        }

        return data
    }

}