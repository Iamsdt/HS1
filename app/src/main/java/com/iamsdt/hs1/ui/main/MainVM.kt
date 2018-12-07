/*
 * Copyright (c) Shudipto Trafder
 * Created on 12/7/18 12:30 PM.
 */

package com.iamsdt.hs1.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.iamsdt.hs1.db.Repository
import com.iamsdt.hs1.db.table.CategoryTable

class MainVM(private val repository: Repository) : ViewModel() {


    fun getAllCategory(): LiveData<PagedList<CategoryTable>> {
        val source = repository.getAllCategory()

        return LivePagedListBuilder(source, PAGE_CONFIG).build()
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