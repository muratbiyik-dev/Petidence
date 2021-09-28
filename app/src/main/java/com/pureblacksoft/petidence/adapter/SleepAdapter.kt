package com.pureblacksoft.petidence.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pureblacksoft.petidence.R
import com.pureblacksoft.petidence.data.Sleep
import com.pureblacksoft.petidence.databinding.LinearSleepBinding
import kotlinx.android.synthetic.main.linear_sleep.view.*

class SleepAdapter(private val sleepList: MutableList<Sleep>): RecyclerView.Adapter<SleepAdapter.ViewHolder>()
{
    private lateinit var mContext: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
    {
        mContext = parent.context
        val inflater = LayoutInflater.from(mContext)
        val view = inflater.inflate(R.layout.linear_sleep, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int)
    {
        val sleep = sleepList[position]
        holder.bind(sleep)
    }

    override fun getItemCount(): Int = sleepList.size

    inner class ViewHolder(private val view: View): RecyclerView.ViewHolder(view)
    {
        private val rootSL = LinearSleepBinding.bind(view).root

        fun bind(sleep: Sleep)
        {
            rootSL.tvSleepContentSL.text = sleep.content
        }
    }
}

//PureBlack Software / Murat BIYIK