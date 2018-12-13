/*
 * Copyright (c) Shudipto Trafder
 * Created on 12/7/18 1:51 PM.
 */

package com.iamsdt.hs1.ui.sub

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.iamsdt.hs1.R
import com.iamsdt.hs1.ext.*
import com.iamsdt.hs1.ui.SigninActivity
import kotlinx.android.synthetic.main.activity_sub_cat.*
import kotlinx.android.synthetic.main.content_sub_cat.*
import kotlinx.android.synthetic.main.dialog_cat.view.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class SubCatActivity : AppCompatActivity() {

    private val vm: SubVM by viewModel()

    private val adapter: SubAdapter by inject()

    private lateinit var dialog: AlertDialog

    private var catID = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sub_cat)
        setSupportActionBar(toolbar)

        val user = FirebaseAuth.getInstance().currentUser
        if (user == null)
            toNextActivity(SigninActivity::class)

        catID = intent.getIntExtra(Intent.EXTRA_TEXT, 0)

        detailsRcv.layoutManager = LinearLayoutManager(this)
        detailsRcv.adapter = adapter

        vm.getAllCategory(catID).observe(this, Observer {
            if (it != null && it.isNotEmpty()) {
                regularView()
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

        sub_img.setOnClickListener {
            val txt = sub_et?.editText?.text?.toString() ?: ""
            vm.add(txt, catID)
        }

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun emptyView() {

    }

    private fun regularView() {

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == android.R.id.home) {
            onBackPressed()
        }

        return super.onOptionsItemSelected(item)
    }
}
