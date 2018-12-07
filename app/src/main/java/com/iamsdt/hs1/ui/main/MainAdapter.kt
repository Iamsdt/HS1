/*
 * Copyright (c) Shudipto Trafder
 * Created on 12/7/18 12:30 PM.
 */

package com.iamsdt.hs1.ui.main

import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.iamsdt.hs1.R
import com.iamsdt.hs1.db.Repository
import com.iamsdt.hs1.db.table.CategoryTable
import com.iamsdt.hs1.ui.sub.SubCatActivity
import com.iamsdt.hs1.utils.ioThread
import kotlinx.android.synthetic.main.categorylist.view.*

class MainAdapter(
    private val repository: Repository,
    private val context: Context
) : PagedListAdapter<CategoryTable, MainAdapter.VH>(diffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {

        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.categorylist, parent, false)

        return VH(view)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {

        val model: CategoryTable? = getItem(position)

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


    inner class VH(view: View) : RecyclerView.ViewHolder(view) {

        private val cat: TextView = view.catTV
        private val num: TextView = view.numTV

        fun bind(model: CategoryTable) {
            //bind with view
            cat.text = model.cat
            setTextCount(num, model.id)
        }
    }

    companion object {

        val diffUtil = object : DiffUtil.ItemCallback<CategoryTable>() {
            override fun areItemsTheSame(oldItem: CategoryTable, newItem: CategoryTable): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: CategoryTable, newItem: CategoryTable): Boolean {
                return oldItem == newItem
            }

        }
    }

    fun setTextCount(tv: TextView, id: Int) {
        ioThread {
            val li = repository.getCatCount(id)
            Handler(Looper.getMainLooper()).post {
                tv.text = (li.size).toString()
            }
        }
    }
}