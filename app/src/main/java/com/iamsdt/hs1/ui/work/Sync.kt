package com.iamsdt.hs1.ui.work

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.google.firebase.firestore.FirebaseFirestore
import com.iamsdt.hs1.db.dao.CategoryDao
import com.iamsdt.hs1.db.table.CategoryTable
import com.iamsdt.hs1.utils.CatDB
import com.iamsdt.hs1.utils.ioThread
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject

class SyncCat(context: Context, workerParameters: WorkerParameters) :
        Worker(context, workerParameters), KoinComponent {

    private val categoryDao: CategoryDao by inject()

    private var state = Result.SUCCESS

    override fun doWork(): Result {
        val store = FirebaseFirestore.getInstance()

        val catRef = store.collection(CatDB.NAME)
        catRef.get().addOnCompleteListener {
            if (it.isSuccessful) {
                val cat =
                        it.result?.toObjects(CategoryTable::class.java) ?: emptyList()

                if (cat.isEmpty()) state = Result.RETRY

                ioThread {
                    cat.filter { t -> t != null }
                            .map { c ->
                                val table: CategoryTable? = categoryDao.searchCat(c.cat)
                                if (table == null) {
                                    categoryDao.add(c)
                                }
                            }
                }
            } else {
                state = Result.RETRY
            }
        }

        return Result.SUCCESS

    }

}