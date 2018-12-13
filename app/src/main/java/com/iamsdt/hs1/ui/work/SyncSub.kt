package com.iamsdt.hs1.ui.work

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.google.firebase.firestore.FirebaseFirestore
import com.iamsdt.hs1.db.dao.SubCategoryDao
import com.iamsdt.hs1.db.table.SubCategoryTable
import com.iamsdt.hs1.utils.SubcatDB
import com.iamsdt.hs1.utils.ioThread
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject

class SyncSub(context: Context, workerParameters: WorkerParameters) :
        Worker(context, workerParameters), KoinComponent {

    private val subcategoryDao: SubCategoryDao by inject()

    private var state = Result.SUCCESS

    override fun doWork(): Result {
        val store = FirebaseFirestore.getInstance()

        val catRef = store.collection(SubcatDB.NAME)
        catRef.get().addOnCompleteListener {
            if (it.isSuccessful) {
                val cat =
                        it.result?.toObjects(SubCategoryTable::class.java) ?: emptyList()

                if (cat.isEmpty()) state = Result.RETRY

                ioThread {
                    cat.filter { t -> t != null }
                            .map { c ->
                                val table: SubCategoryTable? = subcategoryDao.searchSub(c.sub)
                                if (table == null) {
                                    subcategoryDao.add(c)
                                }
                            }
                }

            } else state = Result.RETRY
        }

        return state

    }

}