package com.iamsdt.hs1.ui.main

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.core.view.isGone
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.navigation.NavigationView
import com.iamsdt.hs1.R
import com.iamsdt.hs1.ext.*
import com.iamsdt.hs1.ui.sub.SubCatActivity
import kotlinx.android.synthetic.main.activity_cat.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.dialog_cat.view.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class CatActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {


    private val vm: MainVM by viewModel()

    private val adapter: MainAdapter by inject()

    lateinit var dialog: AlertDialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cat)
        setSupportActionBar(main_toolbar)


        mainRcv.layoutManager = LinearLayoutManager(this)

        mainRcv.adapter = adapter


        vm.getAllCategory().observe(this, Observer {
            if (it != null && it.isNotEmpty()) {
                regularView()
                adapter.submitList(it)
            } else {
                emptyView()
            }
        })

        vm.dialogStatus.observe(this, Observer {
            it?.let { model ->
                if (model.status == 1) {
                    if (::dialog.isInitialized && dialog.isShowing) dialog.dismiss()
                    showToast(ToastType.SUCCESSFUL, model.title)
                }
            }
        })


        fab.setOnClickListener {
            showDialog()
        }

        val toggle = object : ActionBarDrawerToggle(
                this, drawer_layout, main_toolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close) {

            override fun onDrawerOpened(drawerView: View) {
                super.onDrawerOpened(drawerView)
                val manager = getSystemService(Context.INPUT_METHOD_SERVICE)
                        as InputMethodManager

                manager.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
            }
        }

        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)
    }

    private fun regularView() {
        regular.show()
        empty.gone()
    }

    private fun emptyView() {
        regular.gone()
        empty.show()
    }

    private fun showDialog() {

        val view = LayoutInflater.from(this)
                .inflate(R.layout.dialog_cat, mainLay, false)

        val builder = AlertDialog.Builder(this)
        builder.setView(view)

        val et = view.dialogEt
        val bt = view.dialog_btn

        bt.setOnClickListener {
            val txt = et?.editText?.text?.toString() ?: ""

            if (txt.isEmpty() || txt.length <= 3) {
                et.error = "Please input correctly"
            } else {
                vm.add(txt)
            }
        }

        dialog = builder.create()
        dialog.setCanceledOnTouchOutside(false)
        dialog.show()
    }

    override fun onBackPressed() {
        when {
            drawer_layout.isDrawerOpen(GravityCompat.START) -> drawer_layout.closeDrawer(GravityCompat.START)
            fab.isGone -> {
                fab.show()
            }
            else -> super.onBackPressed()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_subcat -> toNextActivity(SubCatActivity::class)
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }
}
