package com.iamsdt.hs1.ui.edit

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.iamsdt.hs1.R
import com.iamsdt.hs1.db.table.CategoryTable
import com.iamsdt.hs1.db.table.SubCategoryTable
import com.iamsdt.hs1.ext.ToastType
import com.iamsdt.hs1.ext.showToast
import kotlinx.android.synthetic.main.activity_edit.*
import org.koin.android.ext.android.inject

class EditActivity : AppCompatActivity() {

    var id = 0

    var catStatus = false

    val vm: EditVM by inject()

    var model: Any = Any()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)
        setSupportActionBar(toolbar)

        var txt = intent.getStringExtra(Intent.EXTRA_TEXT) ?: ""
        if (txt.contains("Cat:")) {
            catStatus = true
            txt = txt.removePrefix("Cat:")
        }

        id = txt.toInt()

        vm.getData(id, catStatus).observe(this, Observer { data ->
            when (data) {
                is CategoryTable -> {
                    edit_et?.editText?.setText(data.cat)
                    model = data
                }

                is SubCategoryTable -> {
                    edit_et?.editText?.setText(data.sub)
                    model = data
                }
            }
        })

        vm.status.observe(this, Observer {
            it?.let { m ->
                showToast(ToastType.SUCCESSFUL, m.title)
                finish()
            }
        })

        button.setOnClickListener {
            val up = edit_et?.editText?.text?.toString() ?: ""
            if (up.isEmpty()) {
                edit_et?.error = "Input is not correct"
                return@setOnClickListener
            }

            vm.update(model)
        }

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        val id = item.itemId
        if (id == android.R.id.home) {
            onBackPressed()
            //setResult(Activity.RESULT_OK)
            finish()

        } else if (id == R.id.action_delete) {
            vm.delete(model)
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.edit, menu)
        return true
    }

}
