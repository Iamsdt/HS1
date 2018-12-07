/*
 * Copyright (c) Shudipto Trafder
 * Created on 12/7/18 4:18 PM.
 */

package com.iamsdt.hs1.ext

import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import es.dmoral.toasty.Toasty

enum class ToastType{
    INFO,
    ERROR,
    SUCCESSFUL,
    WARNING
}

fun AppCompatActivity.showToast(
        type: ToastType,
        message: String,
        time:Int = Toast.LENGTH_SHORT,
        withIcon:Boolean = true){

    when (type) {
        ToastType.INFO -> Toasty.info(this,message,time,withIcon).show()
        ToastType.ERROR -> Toasty.error(this,message,time,withIcon).show()
        ToastType.SUCCESSFUL -> Toasty.success(this,message,time,withIcon).show()
        ToastType.WARNING -> Toasty.warning(this,message,time,withIcon).show()
    }
}

fun Fragment.showToast(
        type: ToastType,
        message: String,
        time:Int = Toast.LENGTH_SHORT,
        withIcon:Boolean = true){

    when (type) {
        ToastType.INFO -> Toasty.info(context!!,message,time,withIcon).show()
        ToastType.ERROR -> Toasty.error(context!!,message,time,withIcon).show()
        ToastType.SUCCESSFUL -> Toasty.success(context!!,message,time,withIcon).show()
        ToastType.WARNING -> Toasty.warning(context!!,message,time,withIcon).show()
    }
}