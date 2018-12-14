package com.iamsdt.hs1.di

import androidx.room.Room
import com.iamsdt.hs1.db.MyDatabase
import com.iamsdt.hs1.db.Repository
import com.iamsdt.hs1.db.dao.CategoryDao
import com.iamsdt.hs1.db.dao.MyTableDao
import com.iamsdt.hs1.db.dao.SubCategoryDao
import com.iamsdt.hs1.ui.add.InsertVm
import com.iamsdt.hs1.ui.cat.CatAdapter
import com.iamsdt.hs1.ui.cat.CatVM
import com.iamsdt.hs1.ui.details.DetailsVM
import com.iamsdt.hs1.ui.edit.EditVM
import com.iamsdt.hs1.ui.main.MainVM
import com.iamsdt.hs1.ui.sub.SubAdapter
import com.iamsdt.hs1.ui.sub.SubVM
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
        CatAdapter(get(), androidContext())
    }

    single {
        SubAdapter(get(), androidContext())
    }
}

val vm = module {
    viewModel { CatVM(get()) }
    viewModel { SubVM(get()) }
    viewModel { InsertVm(get()) }
    viewModel { MainVM(get()) }
    viewModel { EditVM(get()) }
    viewModel { DetailsVM(get()) }
}
