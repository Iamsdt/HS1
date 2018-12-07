/*
 * Copyright (c) Shudipto Trafder
 * Created on 12/7/18 5:54 PM.
 */

package com.iamsdt.hs1.ui

import android.annotation.SuppressLint
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity

@SuppressLint("Registered")
open class BaseActivity : AppCompatActivity() {


    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == android.R.id.home) {
            onBackPressed()
        }

        return super.onOptionsItemSelected(item)
    }

}