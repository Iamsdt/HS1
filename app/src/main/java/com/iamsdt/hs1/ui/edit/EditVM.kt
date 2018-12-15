package com.iamsdt.hs1.ui.edit

import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.iamsdt.hs1.db.Repository
import com.iamsdt.hs1.db.table.CategoryTable
import com.iamsdt.hs1.db.table.MyTable
import com.iamsdt.hs1.db.table.SubCategoryTable
import com.iamsdt.hs1.ext.SingleLiveEvent
import com.iamsdt.hs1.utils.*
import com.iamsdt.hs1.utils.model.EventMessage
import timber.log.Timber

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

    fun add(title: String, des: String, type: PostType, link: String = "", img: String = "", model: MyTable) {
        ioThread {

            val table = model.copy(title = title, des = des, type = type, link = link, img = img)

            val ref =
                    FirebaseFirestore.getInstance().collection(MainDB.NAME)
                            .document(table.ref)

            val map = mapOf(
                    Pair("id", table.id),
                    Pair("title", table.title),
                    Pair("des", table.des),
                    Pair("type", "${table.type}"),
                    Pair("link", table.link),
                    Pair("img", table.img),
                    Pair("category", table.category),
                    Pair("categoryID", table.categoryID),
                    Pair("subCategory", table.subCategory),
                    Pair("subCategoryID", table.subCategoryID),
                    Pair("ref", table.ref)
            )

            ref.update(map).addOnCompleteListener {
                if (it.isSuccessful) Timber.i("Data insert uploaded")
                else Timber.i("Data insert failed")
            }

            val insert = repo.updateMyTable(table)
            if (insert > 0) {
                status.postValue(EventMessage("Updated Successfully", 1))
            }
        }
    }

    fun getMyTable(id: Int) = repo.getDetails(id)

}
