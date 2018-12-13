package com.iamsdt.hs1.ui.edit

import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.iamsdt.hs1.db.Repository
import com.iamsdt.hs1.db.table.CategoryTable
import com.iamsdt.hs1.db.table.SubCategoryTable
import com.iamsdt.hs1.ext.SingleLiveEvent
import com.iamsdt.hs1.utils.CatDB
import com.iamsdt.hs1.utils.SubcatDB
import com.iamsdt.hs1.utils.ioThread
import com.iamsdt.hs1.utils.model.EventMessage

class EditVM(private val repo: Repository) : ViewModel() {

    val status = SingleLiveEvent<EventMessage>()

    private val store = FirebaseFirestore.getInstance()

    fun getData(id: Int, status: Boolean) = if (!status) {
        repo.getSubcategory(id)
    } else repo.getCategory(id)

    fun update(model: Any) {

        when (model) {
            is CategoryTable -> {
                // complete: 12/13/18 update firestore too
                val ref = store.collection(CatDB.NAME).document(model.ref)
                ref.update("cat", model.cat)
                ioThread {
                    val s = repo.updateCat(model)
                    if (s > 0)
                        status.postValue(EventMessage("Category updated", 1))
                }
            }

            is SubCategoryTable -> {
                // complete: 12/13/18 update firestore too
                val ref = store.collection(SubcatDB.NAME).document(model.ref)
                ref.update("cat", model.sub)
                ioThread {
                    val s = repo.updateSubCat(model)
                    if (s > 0)
                        status.postValue(EventMessage("Subcategory updated", 1))
                }
            }
        }

    }

    fun delete(model: Any) {
        when (model) {
            // complete: 12/13/18 delete firestore too
            is CategoryTable -> {
                val ref = store.collection(CatDB.NAME).document(model.ref)
                ref.delete()
                ioThread {
                    val s = repo.deleteCat(model)
                    if (s > 0)
                        status.postValue(EventMessage("Category delete", 1))
                }
            }

            is SubCategoryTable -> {
                // complete: 12/13/18 delete firestore too
                val ref = store.collection(SubcatDB.NAME)
                        .document(model.ref)
                ref.delete()

                ioThread {
                    val s = repo.deleteSubCat(model)
                    if (s > 0)
                        status.postValue(EventMessage("Subcategory delete", 1))
                }
            }
        }
    }

}
