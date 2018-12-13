package com.iamsdt.hs1.ui.edit

import androidx.lifecycle.ViewModel
import com.iamsdt.hs1.db.Repository
import com.iamsdt.hs1.db.table.CategoryTable
import com.iamsdt.hs1.db.table.SubCategoryTable
import com.iamsdt.hs1.ext.SingleLiveEvent
import com.iamsdt.hs1.utils.ioThread
import com.iamsdt.hs1.utils.model.EventMessage

class EditVM(private val repo: Repository) : ViewModel() {

    val status = SingleLiveEvent<EventMessage>()

    fun getData(id: Int, status: Boolean) = if (!status) {
        repo.getSubcategory(id)
    } else repo.getCategory(id)

    fun update(model: Any) {

        when (model) {
            is CategoryTable -> {
                // TODO: 12/13/18 update firestore too
                ioThread {
                    val s = repo.updateCat(model)
                    if (s > 0)
                        status.postValue(EventMessage("Category updated", 1))
                }
            }

            is SubCategoryTable -> {
                // TODO: 12/13/18 update firestore too
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
            // TODO: 12/13/18 delete firestore too
            is CategoryTable -> {
                ioThread {
                    val s = repo.deleteCat(model)
                    if (s > 0)
                        status.postValue(EventMessage("Category delete", 1))
                }
            }

            is SubCategoryTable -> {
                // TODO: 12/13/18 delete firestore too
                ioThread {
                    val s = repo.deleteSubCat(model)
                    if (s > 0)
                        status.postValue(EventMessage("Subcategory delete", 1))
                }
            }
        }
    }

}
