/*
 * Copyright (c) Shudipto Trafder
 * Created on 12/7/18 12:26 PM.
 */

package com.iamsdt.hs1

import android.app.Application
import com.crashlytics.android.Crashlytics
import com.iamsdt.hs1.di.adapterMOdule
import com.iamsdt.hs1.di.dbModule
import com.iamsdt.hs1.di.repoModule
import com.iamsdt.hs1.di.vm
import com.iamsdt.hs1.ext.DebugLogTree
import com.rohitss.uceh.UCEHandler
import io.fabric.sdk.android.Fabric
import org.koin.android.ext.android.startKoin
import timber.log.Timber


class MyApp : Application() {

    override fun onCreate() {
        super.onCreate()

        Fabric.with(applicationContext, Crashlytics())

        UCEHandler.Builder(applicationContext).build()

        Timber.plant(DebugLogTree())

        startKoin(this, listOf(dbModule, repoModule, vm, adapterMOdule))
    }

    /*
    registerResGeneratingTask is deprecated, use registerGeneratedResFolders(FileCollection)
     */

}