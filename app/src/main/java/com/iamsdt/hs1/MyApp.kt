/*
 * Copyright (c) Shudipto Trafder
 * Created on 12/7/18 12:26 PM.
 */

package com.iamsdt.hs1

import android.app.Application
import com.iamsdt.hs1.di.adapterMOdule
import com.iamsdt.hs1.di.dbModule
import com.iamsdt.hs1.di.repoModule
import com.iamsdt.hs1.di.vm
import com.rohitss.uceh.UCEHandler
import org.koin.android.ext.android.startKoin
import timber.log.Timber


class MyApp : Application() {

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())

            UCEHandler.Builder(this).build()
        }


        startKoin(this, listOf(dbModule, repoModule, vm, adapterMOdule))
    }

}