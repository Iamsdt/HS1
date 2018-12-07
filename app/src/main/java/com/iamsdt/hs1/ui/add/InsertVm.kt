/*
 * Copyright (c) Shudipto Trafder
 * Created on 12/7/18 6:31 PM.
 */

package com.iamsdt.hs1.ui.add

import androidx.lifecycle.ViewModel
import com.iamsdt.hs1.db.Repository
import com.iamsdt.hs1.db.table.MyTable
import com.iamsdt.hs1.ext.SingleLiveEvent
import com.iamsdt.hs1.utils.ioThread
import com.iamsdt.hs1.utils.model.EventMessage

class InsertVm(private val repository: Repository) : ViewModel() {

    val status = SingleLiveEvent<EventMessage>()

    fun add(title: String, type: String, link: String = "", img: String = "", subID: Int) {
        ioThread {
            val sub = repository.getSubcat(subID)
            val cat = repository.getCat(sub.categoryID)

            val table = MyTable(
                0, title, type, link, img, cat.cat, cat.id, sub.sub, subID
            )

            // TODO: 12/7/18 add to firestore

            val insert = repository.addMyTable(table)
            if (insert > 0) {
                status.postValue(EventMessage("Inserted Successfully", 1))
            }
        }
    }

}