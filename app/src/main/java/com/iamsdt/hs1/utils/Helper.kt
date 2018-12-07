/*
 * Copyright (c) Shudipto Trafder
 * Created on 12/7/18 5:00 PM.
 */

package com.iamsdt.hs1.utils

import android.os.AsyncTask

fun ioThread(f: () -> Unit) {
    AsyncTask.execute(f)
}