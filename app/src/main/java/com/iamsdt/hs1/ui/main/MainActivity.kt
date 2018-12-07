/*
 * Copyright (c) Shudipto Trafder
 * Created on 12/7/18 12:57 PM.
 */

package com.iamsdt.hs1.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.iamsdt.hs1.R
import com.iamsdt.hs1.ext.ToastType
import com.iamsdt.hs1.ext.gone
import com.iamsdt.hs1.ext.show
import com.iamsdt.hs1.ext.showToast
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.dialog_cat.view.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private val vm: MainVM by viewModel()

    private val adapter: MainAdapter by inject()

    lateinit var dialog: AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        mainRcv.layoutManager = LinearLayoutManager(this)

        mainRcv.adapter = adapter


        vm.getAllCategory().observe(this, Observer {
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


        fab.setOnClickListener {
            showDialog()
        }
    }

    private fun regularView() {
        regular.show()
        empty.gone()
    }

    private fun emptyView() {
        regular.gone()
        empty.show()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showDialog() {

        val view = LayoutInflater.from(this)
            .inflate(R.layout.dialog_cat, mainLay, false)

        val builder = AlertDialog.Builder(this)
        builder.setView(view)

        val et = view.dialogEt
        val bt = view.dialog_btn

        bt.setOnClickListener {
            val txt = et?.editText?.text?.toString() ?: ""

            if (txt.isEmpty() || txt.length <= 3) {
                et.error = "Please input correctly"
            } else {
                vm.add(txt)
            }
        }

        dialog = builder.create()
        dialog.setCanceledOnTouchOutside(false)
        dialog.show()
    }
}
