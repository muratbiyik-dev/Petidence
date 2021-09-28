package com.pureblacksoft.petidence.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.pureblacksoft.petidence.R
import com.pureblacksoft.petidence.activity.GuideActivity
import com.pureblacksoft.petidence.activity.MainActivity
import com.pureblacksoft.petidence.adapter.PlayAdapter
import com.pureblacksoft.petidence.databinding.FragmentPlayBinding
import com.pureblacksoft.petidence.function.AppFun
import com.pureblacksoft.petidence.veriable.ConstVeriable
import kotlinx.android.synthetic.main.appbar_guide.view.*
import kotlinx.android.synthetic.main.fragment_play.view.*

class PlayFragment: Fragment(R.layout.fragment_play)
{
    private lateinit var mContext: Context
    private lateinit var mActivity: GuideActivity
    private lateinit var rootPF1: View
    private lateinit var playAdapter: PlayAdapter

    override fun onAttach(context: Context)
    {
        super.onAttach(context)

        mContext = requireContext()
        mActivity = requireActivity() as GuideActivity
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        val view = inflater.inflate(R.layout.fragment_play, container, false)
        val binding = FragmentPlayBinding.bind(view)
        rootPF1 = binding.root

        //region Toolbar
        rootPF1.tvGuideGAB.text = getString(R.string.Play_Title)
        rootPF1.ivGuideGAB.setImageResource(R.drawable.ic_play_bwhite_32)
        //endregion

        //region Header
        val ivHeaderPF1 = rootPF1.ivHeaderPF1

        when(mActivity.guidePetTypeId)
        {
            ConstVeriable.CAT_PET_TYPE_ID -> ivHeaderPF1.setImageResource(R.drawable.img_header_play_cat)
            ConstVeriable.DOG_PET_TYPE_ID -> ivHeaderPF1.setImageResource(R.drawable.img_header_play_dog)
            ConstVeriable.BIRD_PET_TYPE_ID -> ivHeaderPF1.setImageResource(R.drawable.img_header_play_bird)
            ConstVeriable.FISH_PET_TYPE_ID -> ivHeaderPF1.setImageResource(R.drawable.img_header_play_fish)
        }
        //endregion

        //region RecyclerView
        val recyclerViewPF1 = rootPF1.recyclerViewPF1
        val linearManager = LinearLayoutManager(mContext)
        linearManager.orientation = LinearLayoutManager.VERTICAL
        recyclerViewPF1.layoutManager = linearManager

        val divider = DividerItemDecoration(mContext, linearManager.orientation)
        val dividerDrawable = ContextCompat.getDrawable(mContext, R.drawable.shape_divider_app)
        if (dividerDrawable != null)
        {
            divider.setDrawable(dividerDrawable)
            recyclerViewPF1.addItemDecoration(divider)
        }
        //endregion

        //region playAdapter
        fun setPlayAdapter()
        {
            playAdapter = PlayAdapter(MainActivity.petidenceDB.readPetPlayList(mActivity.guidePetTypeId))
            recyclerViewPF1.adapter = playAdapter

            //region Empty Adapter Control
            if (playAdapter.itemCount == 0)
            {
                AppFun.setVisibility(rootPF1.tvNoDataPF1, true)
            }
            else
            {
                AppFun.setVisibility(rootPF1.tvNoDataPF1, false)
            }
            //endregion
        }
        //endregion

        //region Get Plays
        setPlayAdapter()
        //endregion

        return view
    }
}

//PureBlack Software / Murat BIYIK