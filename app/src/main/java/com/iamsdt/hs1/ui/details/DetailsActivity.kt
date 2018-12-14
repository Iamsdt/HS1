package com.iamsdt.hs1.ui.details

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.iamsdt.hs1.R
import com.iamsdt.hs1.db.table.MyTable
import com.iamsdt.hs1.ext.ToastType
import com.iamsdt.hs1.ext.gone
import com.iamsdt.hs1.ext.show
import com.iamsdt.hs1.ext.showToast
import com.iamsdt.hs1.utils.PostType
import com.pierfrancescosoffritti.androidyoutubeplayer.player.listeners.AbstractYouTubePlayerListener
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
                    details_link.text = m.link
                } else details_link.gone()

                when (m.type) {
                    PostType.LINK -> {
                        details_videoView.gone()
                        details_Img.gone()
                    }

                    PostType.VIDEO -> {
                        details_videoView.show()
                        details_Img.gone()

                        initialize(m)
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

    private fun initialize(m: MyTable) {
        lifecycle.addObserver(details_videoView)
        details_videoView.initialize({ initializedYouTubePlayer ->
            initializedYouTubePlayer.addListener(object : AbstractYouTubePlayerListener() {
                override fun onReady() {
                    initializedYouTubePlayer.loadVideo(getId(m.link), 0f)
                }
            })
        }, true)

    }

    fun getId(link: String): String {
        val base = "https://www.youtube.com/watch?v="

        if (link.contains(base)) {
            val id = link.removePrefix(base)
            showToast(ToastType.INFO, id, Toast.LENGTH_LONG)
            return id
        }

        val base2 = "https://youtu.be/"

        if (link.contains(base2)) {
            val id = link.removePrefix(base2)
            showToast(ToastType.INFO, id, Toast.LENGTH_LONG)
            return id
        }

        return link
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == android.R.id.home) {
            onBackPressed()
        }

        return super.onOptionsItemSelected(item)
    }


}