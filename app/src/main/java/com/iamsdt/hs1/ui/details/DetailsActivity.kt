package com.iamsdt.hs1.ui.details

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.iamsdt.hs1.R
import com.iamsdt.hs1.ext.gone
import com.iamsdt.hs1.ext.show
import com.iamsdt.hs1.utils.PostType
import kotlinx.android.synthetic.main.activity_details.*
import kotlinx.android.synthetic.main.content_details.*
import org.koin.android.ext.android.inject

class DetailsActivity : AppCompatActivity() {


    var id = 0

    private val vm: DetailsVM by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)
        setSupportActionBar(toolbar)

        id = intent.getIntExtra(Intent.EXTRA_TEXT, 0)


        vm.getDetails(id).observe(this, Observer {
            it?.let { m ->
                details_title.text = m.title

                if (m.des.isNotEmpty()) {
                    details_des.text = m.des
                } else details_des.gone()

                if (m.link.isNotEmpty()) {
                    details_link.text = m.title
                } else details_link.gone()

                when (m.type) {
                    PostType.LINK -> {
                        details_videoView.gone()
                        details_Img.gone()
                    }

                    PostType.VIDEO -> {
                        details_videoView.show()
                        details_Img.gone()
                        details_videoView.setVideoURI(m.link.toUri())
                    }

                    PostType.IMAGE -> {
                        details_videoView.gone()
                        details_Img.show()
                        Glide.with(this).load(m.img.toUri()).into(details_Img)
                    }
                }

                //cat
                val cat = "Category:${m.category}"
                val sub = "Subcategory: ${m.subCategory}"

                details_cat.text = cat
                details_sub.text = sub

            }
        })

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == android.R.id.home) {
            onBackPressed()
        }

        return super.onOptionsItemSelected(item)
    }


}