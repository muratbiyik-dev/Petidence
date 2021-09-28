package com.pureblacksoft.petidence.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pureblacksoft.petidence.R
import com.pureblacksoft.petidence.data.Play
import com.pureblacksoft.petidence.databinding.LinearPlayBinding
import kotlinx.android.synthetic.main.linear_play.view.*

class PlayAdapter(private val playList: MutableList<Play>): RecyclerView.Adapter<PlayAdapter.ViewHolder>()
{
    private lateinit var mContext: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
    {
        mContext = parent.context
        val inflater = LayoutInflater.from(mContext)
        val view = inflater.inflate(R.layout.linear_play, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int)
    {
        val play = playList[position]
        holder.bind(play)
    }

    override fun getItemCount(): Int = playList.size

    inner class ViewHolder(private val view: View): RecyclerView.ViewHolder(view)
    {
        private val rootPL1 = LinearPlayBinding.bind(view).root

        fun bind(play: Play)
        {
            rootPL1.tvPlayContentPL1.text = play.content
        }
    }
}

//PureBlack Software / Murat BIYIK