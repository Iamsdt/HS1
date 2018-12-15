package com.iamsdt.hs1.ui.main

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.provider.SearchRecentSuggestions
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.GravityCompat
import androidx.core.view.isGone
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.iamsdt.hs1.R
import com.iamsdt.hs1.ext.gone
import com.iamsdt.hs1.ext.show
import com.iamsdt.hs1.ext.toNextActivity
import com.iamsdt.hs1.ui.AppAbout
import com.iamsdt.hs1.ui.SigninActivity
import com.iamsdt.hs1.ui.cat.CatActivity
import com.iamsdt.hs1.ui.search.MySuggestionProvider
import com.iamsdt.hs1.ui.sub.SubCatActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.content_main.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {


    private val vm: MainVM by viewModel()

    lateinit var searchView: SearchView
    lateinit var suggestions: SearchRecentSuggestions

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(main_toolbar)

        val user = FirebaseAuth.getInstance().currentUser
        if (user == null)
            toNextActivity(SigninActivity::class)


        mainRcv.layoutManager = LinearLayoutManager(this)
        val adapter = MainAdapter(this)
        mainRcv.adapter = adapter

        vm.data.observe(this, Observer {
            if (it != null && it.isNotEmpty()) {
                regularView()
                adapter.submitList(it)
            } else {
                emptyView()
            }
        })

        setUpSearchView()

        fab.setOnClickListener {
            toNextActivity(CatActivity::class)
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

    private fun setUpSearchView() {
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchView = searchViewN
        suggestions = SearchRecentSuggestions(this,
                MySuggestionProvider.AUTHORITY, MySuggestionProvider.MODE)

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))

        searchView.setIconifiedByDefault(false)
        searchView.isQueryRefinementEnabled = true
        searchView.requestFocus(1)

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                //todo go to details

                //suggestions.saveRecentQuery(query, null)
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                if (newText.isNotEmpty()) {
                    vm.search(newText)
                }

                return true
            }
        })

        searchView.setOnSuggestionListener(object : SearchView.OnSuggestionListener {
            override fun onSuggestionSelect(position: Int): Boolean {
                //todo go to details
                return true
            }

            override fun onSuggestionClick(position: Int): Boolean {
                val selectedView = searchView.suggestionsAdapter
                val cursor = selectedView.getItem(position) as Cursor
                val index = cursor.getColumnIndexOrThrow(SearchManager.SUGGEST_COLUMN_TEXT_1)
                searchView.setQuery(cursor.getString(index), true)
                return true
            }
        })

    }

    override fun onNewIntent(intent: Intent?) {
        if (Intent.ACTION_SEARCH == intent?.action) {
            val query = intent.getStringExtra(SearchManager.QUERY)
            if (::searchView.isInitialized) {
                searchView.setQuery(query, true)
            }
        }
    }

    private fun regularView() {
        regular.show()
        empty.gone()
    }

    private fun emptyView() {
        regular.gone()
        empty.show()
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
            R.id.nav_cat -> toNextActivity(CatActivity::class)
            R.id.nav_about -> toNextActivity(AppAbout::class)
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }
}