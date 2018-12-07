/*
 * Copyright (c) Shudipto Trafder
 * Created on 12/7/18 4:18 PM.
 */

package com.iamsdt.hs1.ext

import android.os.Build
import android.util.Log
import timber.log.Timber

class DebugLogTree:Timber.DebugTree(){

    override fun createStackElementTag(element: StackTraceElement): String? {
        //super.createStackElementTag(element)
        return "Class:${element.className} - Method:${element.methodName} -" +
                " LN:${element.lineNumber}"
    }

    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {

        var p = priority

        if (Build.MANUFACTURER == "HUAWEI" || Build.MANUFACTURER == "samsung") {
            if (p == Log.VERBOSE || p == Log.DEBUG || p == Log.INFO)
                p = Log.ERROR
        }

        super.log(p, tag, message, t)
    }
}