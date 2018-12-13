/*
 * Copyright (c) Shudipto Trafder
 * Created on 12/7/18 6:31 PM.
 */

package com.iamsdt.hs1.ui.add

import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.iamsdt.hs1.db.Repository
import com.iamsdt.hs1.db.table.MyTable
import com.iamsdt.hs1.ext.SingleLiveEvent
import com.iamsdt.hs1.utils.MainDB
import com.iamsdt.hs1.utils.PostType
import com.iamsdt.hs1.utils.ioThread
import com.iamsdt.hs1.utils.model.EventMessage
import timber.log.Timber

class InsertVm(private val repository: Repository) : ViewModel() {

    val status = SingleLiveEvent<EventMessage>()

    fun add(title: String, des: String, type: PostType, link: String = "", img: String = "", subID: Int) {
        ioThread {
            val sub = repository.getSubcat(subID)
            val cat = repository.getCat(sub.categoryID)

            val table = MyTable(
                    0, title, des, type, link, img, cat.cat, cat.id, sub.sub, subID, title
            )

            val ref =
                    FirebaseFirestore.getInstance().collection(MainDB.NAME)
                            .document(table.ref)

            ref.set(table).addOnCompleteListener {
                if (it.isSuccessful) Timber.i("Data insert uploaded")
                else Timber.i("Data insert failed")
            }

            val insert = repository.addMyTable(table)
            if (insert > 0) {
                status.postValue(EventMessage("Inserted Successfully", 1))
            }
        }
    }

}