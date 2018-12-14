package com.iamsdt.hs1.ui.cat

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.iamsdt.hs1.R
import com.iamsdt.hs1.ext.ToastType
import com.iamsdt.hs1.ext.showToast
import com.iamsdt.hs1.ext.toNextActivity
import com.iamsdt.hs1.ui.SigninActivity
import kotlinx.android.synthetic.main.activity_cat.*
import kotlinx.android.synthetic.main.content_cat.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class CatActivity : AppCompatActivity() {


    private val vm: CatVM by viewModel()

    private val adapter: CatAdapter by inject()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cat)
        setSupportActionBar(toolbar)

        val user = FirebaseAuth.getInstance().currentUser
        if (user == null)
            toNextActivity(SigninActivity::class)

        catRCV.layoutManager = LinearLayoutManager(this)

        catRCV.adapter = adapter

        vm.getAllCategory().observe(this, Observer {
            if (it != null && it.isNotEmpty()) {
                adapter.submitList(it)
            } else {
                //// TODO: 12/13/18 add empty view
            }
        })

        vm.status.observe(this, Observer {
            if (it != null && it.status != 0) {
                showToast(ToastType.SUCCESSFUL, it.title)
                cat_et.editText?.text?.clear()
            }
        })

        cat_btn.setOnClickListener {
            val title = cat_et.editText?.text?.toString() ?: ""
            if (title.isNotEmpty()) vm.add(title)
            else cat_et.error = "Input valid category"
        }

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == android.R.id.home) {
            onBackPressed()
        }

        return super.onOptionsItemSelected(item)
    }
}
