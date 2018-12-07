package com.iamsdt.hs1.di

import androidx.room.Room
import com.iamsdt.hs1.db.MyDatabase
import com.iamsdt.hs1.db.Repository
import com.iamsdt.hs1.db.dao.CategoryDao
import com.iamsdt.hs1.db.dao.MyTableDao
import com.iamsdt.hs1.db.dao.SubCategoryDao
import com.iamsdt.hs1.ui.main.MainAdapter
import com.iamsdt.hs1.ui.main.MainVM
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module

val dbModule = module {

    single { get<MyDatabase>().myTableDao }
    single { get<MyDatabase>().categoryDao }
    single { get<MyDatabase>().subCategoryDao }

    single {
        Room.databaseBuilder(
            androidContext(),
            MyDatabase::class.java, "MyDatabase"
        )
            .enableMultiInstanceInvalidation()
            .build()
    }
}

val repoModule = module {
    single {
        Repository(
            get() as MyTableDao,
            get() as SubCategoryDao,
            get() as CategoryDao
        )
    }
}

val adapterMOdule = module {
    single {
        MainAdapter(get(), androidContext())
    }
}

val vm = module {
    viewModel { MainVM(get()) }
}
