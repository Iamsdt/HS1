package com.iamsdt.hs1.di

import androidx.room.Room
import androidx.room.RoomDatabase
import com.iamsdt.hs1.db.MyDatabase
import com.iamsdt.hs1.db.Repository
import com.iamsdt.hs1.ui.main.MainAdapter
import com.iamsdt.hs1.ui.main.MainVM
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.Module
import org.koin.dsl.module.module
import kotlin.math.sin

val dbModule = module {

    single { get<MyDatabase>().myTableDao }
    single { get<MyDatabase>().categoryDao }
    single { get<MyDatabase>().subCategoryDao }

    single {
        Room.databaseBuilder(
            androidApplication(),
            MyDatabase::class.java, "MyDatabase"
        )
            .enableMultiInstanceInvalidation()
            .build()
    }
}

val repoModule = module {
    single {
        Repository(get(), get(), get())
    }
}

val adapterMOdule = module {
    MainAdapter(get(), androidContext())
}

val vm = module {
    viewModel { MainVM(get()) }
}
