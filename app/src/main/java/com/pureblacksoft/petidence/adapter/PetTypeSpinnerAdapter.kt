package com.pureblacksoft.petidence.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.pureblacksoft.petidence.R
import com.pureblacksoft.petidence.databinding.LinearPetTypeBinding
import kotlinx.android.synthetic.main.linear_pet_type.view.*

class PetTypeSpinnerAdapter(private val context: Context,
                            private val petIconList: MutableList<Int>,
                            private val petLabelList: MutableList<String>): BaseAdapter()
{
    override fun getItemId(i: Int): Long
    {
        return 0
    }

    override fun getItem(i: Int): Any?
    {
        return null
    }

    override fun getCount(): Int
    {
        return petIconList.size
    }

    override fun getView(position: Int, view: View?, parent: ViewGroup?): View
    {
        val inflater = LayoutInflater.from(context)
        val newView = inflater.inflate(R.layout.linear_pet_type, parent, false)
        val rootPTL = LinearPetTypeBinding.bind(newView).root
        rootPTL.tvPetLabelPTL.text = petLabelList[position]
        rootPTL.ivPetIconPTL.setImageResource(petIconList[position])

        return newView
    }
}

//PureBlack Software / Murat BIYIK