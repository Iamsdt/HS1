package com.iamsdt.hs1.ui.details

import androidx.lifecycle.ViewModel
import com.iamsdt.hs1.db.Repository

class DetailsVM(private val repository: Repository) : ViewModel() {

    fun getDetails(id: Int) =
            repository.getDetails(id)


}