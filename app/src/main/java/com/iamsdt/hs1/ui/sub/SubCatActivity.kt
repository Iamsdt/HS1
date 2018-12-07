/*
 * Copyright (c) Shudipto Trafder
 * Created on 12/7/18 1:51 PM.
 */

package com.iamsdt.hs1.ui.sub

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity;
import com.iamsdt.hs1.R

import kotlinx.android.synthetic.main.activity_sub_cat.*

class SubCatActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sub_cat)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
    }

}
