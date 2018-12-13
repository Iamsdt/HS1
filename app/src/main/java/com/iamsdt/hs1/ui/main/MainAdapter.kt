package com.iamsdt.hs1.ui.main

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.VideoView
import androidx.core.net.toUri
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.iamsdt.hs1.R
import com.iamsdt.hs1.db.table.MyTable
import com.iamsdt.hs1.ext.gone
import com.iamsdt.hs1.ext.show
import com.iamsdt.hs1.ui.sub.SubCatActivity
import com.iamsdt.hs1.utils.PostType
import kotlinx.android.synthetic.main.main_card.view.*

class MainAdapter(
        private val context: Context
) : PagedListAdapter<MyTable, MainAdapter.VH>(diffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {

        val view: View = LayoutInflater.from(parent.context)
                .inflate(R.layout.main_card, parent, false)

        return VH(view)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {

        val model: MyTable? = getItem(position)

        model?.let {
            holder.bind(it)
            holder.itemView.setOnClickListener {
                val intent = Intent(context, SubCatActivity::class.java)
                intent.putExtra(Intent.EXTRA_TEXT, model.id)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                context.startActivity(intent)
            }
        }

    }


    inner class VH(val view: View) : RecyclerView.ViewHolder(view) {

        private val titleTV: TextView = view.main_title
        private val desTV: TextView = view.main_des
        private val linkTV: TextView = view.main_link
        private val catTV: TextView = view.main_cat
        private val subTV: TextView = view.main_sub

        private val video: VideoView = view.main_videoView
        private val img: ImageView = view.main_img

        fun bind(model: MyTable) {
            //bind with view
            titleTV.text = model.title

            if (model.des.isNotEmpty()) {
                desTV.text = model.des
            } else desTV.gone()

            if (model.link.isNotEmpty()) {
                linkTV.text = model.link
            } else linkTV.gone()

            val cat = "Category: ${model.category}"
            val sub = "Subcategory: ${model.subCategory}"
            catTV.text = cat
            subTV.text = sub


            if (model.img.isNotEmpty()) {

                when (model.type) {

                    PostType.IMAGE -> {
                        video.gone()
                        img.show()

                        val ONE_MEGABYTE: Long = 1024 * 1024

                        FirebaseStorage.getInstance()
                                .getReferenceFromUrl(model.link).getBytes(ONE_MEGABYTE)
                                .addOnCompleteListener {
                                    if (it.isSuccessful) {
                                        val byte = it.result
                                        val bit = BitmapFactory.decodeByteArray(byte, 0,
                                                byte?.size ?: 0)
                                        Glide.with(view).load(bit).into(img)
                                    } else {
                                        // TODO: 12/13/18 add error image
                                    }
                                }
                    }

                    PostType.LINK -> {
                        img.gone()
                        video.gone()
                    }

                    PostType.VIDEO -> {
                        img.gone()
                        video.show()
                        video.setVideoPath(model.link)
                    }


                }

            }
        }
    }

    companion object {

        val diffUtil = object : DiffUtil.ItemCallback<MyTable>() {
            override fun areItemsTheSame(oldItem: MyTable, newItem: MyTable): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: MyTable, newItem: MyTable): Boolean {
                return oldItem == newItem
            }

        }
    }
}