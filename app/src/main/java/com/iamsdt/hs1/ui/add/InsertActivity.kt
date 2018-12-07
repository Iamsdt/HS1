/*
 * Copyright (c) Shudipto Trafder
 * Created on 12/7/18 6:25 PM.
 */

package com.iamsdt.hs1.ui.add

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.iamsdt.hs1.R
import com.iamsdt.hs1.ext.ToastType
import com.iamsdt.hs1.ext.showToast
import kotlinx.android.synthetic.main.activity_insert.*
import kotlinx.android.synthetic.main.content_insert.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class InsertActivity : AppCompatActivity() {

    private val vm: InsertVm by viewModel()

    var imgLink = ""

    var subId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_insert)
        setSupportActionBar(toolbar)

        subId = intent.getIntExtra(Intent.EXTRA_TEXT, 0)

        vm.status.observe(this, Observer {
            it?.let { m ->
                if (m.status == 1) {
                    showToast(ToastType.SUCCESSFUL, m.title)
                }
            }
        })

        insertData()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

    }

    private fun insertData() {
        val titleEt = titleEt
        val typeEt = typeEt
        val linkEt = linkEt
        val btn = postBtn
        val img = pickImg

        img.setOnClickListener {
            // TODO: 12/7/18 image picker

        }

        btn.setOnClickListener {
            val title = titleEt.editText?.text?.toString() ?: ""
            val type = typeEt.editText?.text?.toString() ?: ""
            val link = linkEt.editText?.text?.toString() ?: ""

            if (title.isEmpty() || title.length <= 3) {
                titleEt.error = "Tittle is not valid"
                return@setOnClickListener
            }

            if (type.isEmpty() || type.length <= 3) {
                typeEt.error = "Type is not valid"
                return@setOnClickListener
            }

            vm.add(title,type,link,imgLink,subId)

        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == android.R.id.home) {
            onBackPressed()
        }

        return super.onOptionsItemSelected(item)
    }

}
