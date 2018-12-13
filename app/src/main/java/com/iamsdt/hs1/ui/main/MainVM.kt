package com.iamsdt.hs1.ui.main

import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import com.iamsdt.hs1.db.Repository
import com.iamsdt.hs1.ui.cat.CatVM

class MainVM(private val repository: Repository) : ViewModel() {

    fun getALlData() =
            LivePagedListBuilder(repository.allData, CatVM.PAGE_CONFIG).build()

}