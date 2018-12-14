package com.iamsdt.hs1.ui.work

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.google.firebase.firestore.FirebaseFirestore
import com.iamsdt.hs1.db.dao.MyTableDao
import com.iamsdt.hs1.db.table.MyTable
import com.iamsdt.hs1.utils.MainDB
import com.iamsdt.hs1.utils.ioThread
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject

class MainTable(context: Context, workerParameters: WorkerParameters) :
        Worker(context, workerParameters), KoinComponent {

    private val myTable: MyTableDao by inject()

    private var state = Result.SUCCESS

    override fun doWork(): Result {
        val store = FirebaseFirestore.getInstance()

        val catRef = store.collection(MainDB.NAME)
        catRef.get().addOnCompleteListener {
            if (it.isSuccessful) {
                val cat =
                        it.result?.toObjects(MyTable::class.java) ?: emptyList()

                if (cat.isEmpty()) state = Result.RETRY

                ioThread {
                    cat.filter { t -> t != null }
                            .forEach { c ->
                                val table: MyTable? = myTable.search(c.title)
                                if (table == null) {
                                    if (c?.title?.isNotEmpty() == true) {
                                        myTable.add(c)
                                    }
                                } else {
                                    myTable.update(table)
                                }
                            }
                }

            } else state = Result.RETRY
        }

        return state

    }

}