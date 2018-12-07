/*
 * Copyright (c) Shudipto Trafder
 * Created on 12/7/18 1:51 PM.
 */

package com.iamsdt.hs1.ui.sub

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.iamsdt.hs1.R
import com.iamsdt.hs1.ext.ToastType
import com.iamsdt.hs1.ext.gone
import com.iamsdt.hs1.ext.show
import com.iamsdt.hs1.ext.showToast
import com.iamsdt.hs1.ui.BaseActivity
import kotlinx.android.synthetic.main.activity_sub_cat.*
import kotlinx.android.synthetic.main.content_sub_cat.*
import kotlinx.android.synthetic.main.dialog_cat.view.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class SubCatActivity : BaseActivity() {

    private val vm: SubVM by viewModel()

    private val adapter: SubAdapter by inject()

    lateinit var dialog: AlertDialog

    var catID = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sub_cat)
        setSupportActionBar(toolbar)

        catID = intent.getIntExtra(Intent.EXTRA_TEXT, 0)


        detailsRcv.layoutManager = LinearLayoutManager(this)
        detailsRcv.adapter = adapter

        vm.getAllCategory().observe(this, Observer {
            if (it != null && it.isNotEmpty()) {
                adapter.submitList(it)
            } else {
                emptyView()
            }
        })


        vm.dialogStatus.observe(this, Observer {
            it?.let { model ->
                if (model.status == 1) {
                    if (::dialog.isInitialized && dialog.isShowing) dialog.dismiss()
                    showToast(ToastType.SUCCESSFUL, model.title)
                }
            }
        })

        fab.setOnClickListener {
            showDialog()
        }

        supportActionBar?.setDisplayShowHomeEnabled(true)
    }

    private fun emptyView() {
        empty.show()
        regular.gone()
    }

    private fun showDialog() {

        val view = LayoutInflater.from(this)
            .inflate(R.layout.dialog_sub, detailsLay, false)

        val builder = AlertDialog.Builder(this)
        builder.setView(view)

        val et = view.dialogEt
        val bt = view.dialog_btn

        bt.setOnClickListener {
            val txt = et?.editText?.text?.toString() ?: ""

            if (txt.isEmpty() || txt.length <= 3) {
                et.error = "Please input correctly"
            } else {
                vm.add(txt,catID)
            }
        }

        dialog = builder.create()
        dialog.setCanceledOnTouchOutside(false)
        dialog.show()
    }

}
